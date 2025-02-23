from pydantic import BaseModel, Field
import faiss
import sqlite3
import uvicorn
import os
from langchain_ollama import OllamaLLM
from langchain_ollama.chat_models import ChatOllama
from langchain.prompts import PromptTemplate
from langchain_core.prompts import ChatPromptTemplate
from langchain_huggingface import HuggingFaceEmbeddings
from langchain.chains import LLMChain
from fastapi import FastAPI, HTTPException
import math

# Inicializar el servidor FastAPI
server = FastAPI()

# Configuración del modelo Ollama
llm = ChatOllama(model = "llama3.2:3b", temperature = 0)

#Configuración del modelo de embeddings para vectorizar
embeddings_model = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")

#Directorio para guardar los documentos que se envíen
os.makedirs("documentos", exist_ok=True)

#ESTRUCTURAS DE DATOS DE RECEPCIÓN
#Estructura para verificar usuario
class sesionStruct(BaseModel):
    clave:str

#Estructura para consultar datos
class consultaStruct(BaseModel):
    query:str

#Estructura para insertar datos
class InsertarStruct(BaseModel):
    clave: str
    frase_competencia: str

#Definir la estructura de los datos que esperamos del modelo llama3.2
class CompetenciaResponse(BaseModel):
    item: str = Field(description="La competencia aprendida referida en la frase")



#Devolver lista de trabajadores y atributos
def get_trabajadores():
    conn = sqlite3.connect('empresa.db')
    cursor = conn.cursor()
    cursor.execute("SELECT clave, nombre, correo, competencias FROM trabajadores")
    trabajadores = cursor.fetchall()
    conn.close()
    
    if not trabajadores:
        print("No hay trabajadores en la base de datos.")
        return None

    return trabajadores

#Función para comprobar si una competencia ya existe en un usuario
def comprobar_competencia(clave, competencia, umbral=0.7, return_rateo=False):
    conn = sqlite3.connect('empresa.db')
    cursor = conn.cursor()
    cursor.execute("SELECT competencias FROM trabajadores WHERE clave = ?", (clave,))
    resultado = cursor.fetchone()
    conn.close()
    if resultado[0]:
        competencias_existentes = [c for c in [s.strip() for s in resultado[0].split(",")]]
        compvec = embeddings_model.embed_query(competencia)
        for compex in competencias_existentes:
            compexvec = embeddings_model.embed_query(compex)
            if cosine_similarity(compvec, compexvec) >= umbral:
                return True
    return False


# Función para insertar competencias en la base de datos
def insertar_competencia(clave, competencia):
    if comprobar_competencia(clave, competencia):
        return False
    
    conn = sqlite3.connect('empresa.db')
    cursor = conn.cursor()
    cursor.execute("SELECT competencias FROM trabajadores WHERE clave = ?", (clave,))
    resultado = cursor.fetchone()
    nuevostr = resultado[0] + ", " + competencia if resultado[0] else competencia
    cursor.execute("UPDATE trabajadores SET competencias = ? WHERE clave = ?", (nuevostr, clave))
    conn.commit()
    conn.close()
    return True


 
# Función para vectorizar a los trabajadores y la consulta
def vectorize_text(texts, embeddings_model):
    return embeddings_model.embed_documents(texts)



    

#Función para calcular la similitud del coseno entre dos vectores
def cosine_similarity(vec1, vec2):
    dot_product = sum(v1 * v2 for v1, v2 in zip(vec1, vec2))
    norm_vec1 = math.sqrt(sum(v1 * v1 for v1 in vec1))
    norm_vec2 = math.sqrt(sum(v2 * v2 for v2 in vec2))
    if norm_vec1 == 0 or norm_vec2 == 0:
        return 0
    return dot_product / (norm_vec1 * norm_vec2)



#busca un trabajador con la clave proporcionada y en caso de existir devuelve su nombre
def comprobar_inicio_sesion(clave):
    conn = sqlite3.connect('empresa.db')
    cursor = conn.cursor()

    cursor.execute("SELECT nombre FROM trabajadores WHERE clave = ?", (clave,))
    
    resultado = cursor.fetchone()
    conn.close()

    if resultado:
        return resultado[0]
    else:
        return "False"  

#Endpoint para consultar trabajadores con conocimientos sobre un tema específico
@server.post("/consultar")
def consultar_trabajadores(request : consultaStruct):
    trabajadores = get_trabajadores()
    umbral = 0.4
    filtrados = []
    for trabajador in trabajadores:
        clave, nombre, correo, competencias = trabajador
        score = comprobar_competencia(clave, request.query, umbral, return_rateo=True)
        if score and score>umbral:
            filtrados.append((nombre, competencias, score, correo))

    if filtrados:
        filtrados.sort(key=lambda x: x[2], reverse=True)  
        texto =  " ".join([str({"nombre": nombre, "competencias": competencias, "correo":correo}) for nombre, competencias, _, correo in filtrados[:2]])
        prompt_template = """
        "{query}".
        Comenta los trabajadores junto con sus correos las diferentes competencias de cada uno (no uses comillas
        , comillas dobles y usa puntos y seguido)
        """

        prompt = PromptTemplate(input_variables=["query"], template=prompt_template)      
    else:
        prompt_template = """
        No he encontrado trabajadores que se adecuen a la descripción.
        """
        prompt = PromptTemplate(template=prompt_template)
    chain = prompt | llm

    #Obtener respuesta de la IA
    response = chain.invoke({"query": texto})
    return response


#Endpoint para agregar una competencia de un trabajador
@server.post("/insertar")
def agregar_competencia(request: InsertarStruct):
    model_with_structure = llm.with_structured_output(CompetenciaResponse)
    structured_output = model_with_structure.invoke(request.frase_competencia)
    competencia = structured_output.item
    insertar_competencia(request.clave, competencia)


    if insertar_competencia(request.clave, competencia):
        print ("Competencia agregada: ", competencia)

    # Prompt más detallado para una mejor respuesta
    prompt_template = """
        Has indicado que has aprendido sobre lo siguiente: "{query}".
        Describe brevemente esta competencia en una o dos oraciones (no uses comillas simples).
        """

    prompt = PromptTemplate(input_variables=["query"], template=prompt_template)
    chain = prompt | llm

    #Obtener respuesta de la IA
    response = chain.invoke({"query": request.frase_competencia})
    
    return f"Competencia agregada: {competencia}. Respuesta de la IA: {response}"
    
#Endpoint para iniciar sesión
@server.post("/sesion")
def inicio_sesion(sesionRequest: sesionStruct):
    value = comprobar_inicio_sesion(sesionRequest.clave)
    return value



# Ejecutar servidor
if __name__ == "__main__":
    uvicorn.run(server, host="0.0.0.0", port=8000)
