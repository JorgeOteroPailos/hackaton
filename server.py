from pydantic import BaseModel, Field
import sqlite3
import uvicorn
from langchain_ollama import OllamaLLM
from langchain_ollama.chat_models import ChatOllama
from langchain.prompts import PromptTemplate
from langchain_core.prompts import ChatPromptTemplate
from langchain_huggingface import HuggingFaceEmbeddings  # Modelo de embeddings sin necesidad de clave API
from langchain.chains import LLMChain
from fastapi import FastAPI, HTTPException
import math

server = FastAPI()

#Estructura que recibe en la conexión
class InsertarStruct(BaseModel):
    clave: str
    frase_competencia: str

# Definir la estructura de los datos que esperamos del modelo
class CompetenciaResponse(BaseModel):
    item: str = Field(description="La competencia aprendida referida en la frase")

# Conexión a la base de datos SQLite
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


#CODIGO INSERTAR COMPETENCIAS
# Función para insertar o actualizar competencia en la base de datos
def insertar_o_actualizar_competencia(clave, competencia):
    conn = sqlite3.connect('empresa.db')
    cursor = conn.cursor()

    # Buscar si ya existe un trabajador con el mismo nombre
    cursor.execute("SELECT competencias FROM trabajadores WHERE clave = ?", (clave,))
    trabajador = cursor.fetchone()

    if trabajador:
        # Si ya existe, actualizar la competencia agregando la nueva
        competencias_existentes = trabajador[0]
        # Comprobar si la competencia no está en las competencias existentes
        if competencia not in competencias_existentes:
            competencias_actualizadas = competencias_existentes + ", " + competencia
            cursor.execute("UPDATE trabajadores SET competencias = ? WHERE clave = ?", (competencias_actualizadas, clave))

    conn.commit()
    conn.close()

# Configuración del modelo Ollama
llm = ChatOllama(model = "llama3.2:3b", temperature = 0)


    #CODIGO CONSULTAR COMPETENCIAS
    # Función para vectorizar a los trabajadores y la consulta
def vectorize_text(texts, embeddings_model):
    return embeddings_model.embed_documents(texts)

# Función para encontrar los trabajadores más similares utilizando embeddings
def find_top_matching_workers_with_embeddings(query, trabajadores, top_n=5, embeddings_model=None):
    # Vectoriza la consulta
    query_embedding = embeddings_model.embed_query(query)
    
    # Crear las competencias y nombres de los trabajadores en formato de texto
    #trabajadores_texto = [f"{nombre}: {competencias}" for nombre, competencias in trabajadores]
    competencias_texto = [f"{competencias}" for competencias in trabajadores]
    
    # Vectoriza las competencias de los trabajadores
    worker_embeddings = vectorize_text(competencias_texto, embeddings_model)

    # Calcular similitud de coseno entre la consulta y los trabajadores
    scores = []
    for i, (nombre, competencias) in enumerate(trabajadores):
        score = cosine_similarity(query_embedding, worker_embeddings[i])
        scores.append((nombre, competencias, score))

    # Ordenar por mayor similitud y tomar los X mejores
    scores.sort(key=lambda x: x[2], reverse=True)
    
    return scores[:top_n]

# Función para calcular la similitud del coseno entre dos vectores
def cosine_similarity(vec1, vec2):
    dot_product = sum(v1 * v2 for v1, v2 in zip(vec1, vec2))
    norm_vec1 = math.sqrt(sum(v1 * v1 for v1 in vec1))
    norm_vec2 = math.sqrt(sum(v2 * v2 for v2 in vec2))
    if norm_vec1 == 0 or norm_vec2 == 0:
        return 0
    return dot_product / (norm_vec1 * norm_vec2)

# Configuración del modelo de embeddings sin necesidad de API key
embeddings_model = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")


#Endpoint para consultar trabajadores
@server.post("/consultar")
def consultar_trabajadores(query: str):
    trabajadores = get_trabajadores()

    resultados = find_top_matching_workers_with_embeddings(query, trabajadores, top_n,embeddings_model)
    # Filtrar trabajadores con score < min_similarity
    filtered_workers = [(nombre, competencias, score) for nombre, competencias, score in matching_workers if score >= min_similarity]

    if not filtered_workers:
        raise HTTPException(status_code=404, detail="No se encontraron trabajadores afines")

    return [{"nombre": nombre, "competencias": competencias, "similitud": round(score, 2)} for nombre, competencias, score in filtered_workers]


#  Endpoint para agregar competencia
@server.post("/insertar")
def agregar_competencia(request: InsertarStruct):
    model_with_structure = llm.with_structured_output(CompetenciaResponse)
    structured_output = model_with_structure.invoke(request.frase_competencia)
    competencia = structured_output.item

    insertar_o_actualizar_competencia(request.clave, competencia)
    return {"mensaje": f"Competencia '{competencia}' agregada a {request.nombre}"}


# Ejecutar servidor
if __name__ == "__main__":
    uvicorn.run(server, host="0.0.0.0", port=8000)
