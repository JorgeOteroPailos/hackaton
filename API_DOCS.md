# Documentación de la API de CompetenciApp

CompetenciApp proporciona una API para gestionar competencias de empleados utilizando FastAPI, FAISS y modelos de inteligencia artificial.



## Endpoints disponibles

### Consultar trabajadores con competencias

Busca empleados que tengan habilidades en un área específica.

URL: /consultar

Método: POST

Descripción: Devuelve una lista de empleados que tienen competencias relacionadas con el tema consultado.

Parámetros (JSON Request Body):

query (string, requerido): La competencia a buscar.

Ejemplo de solicitud:

{
  "query": "machine learning"
}

Ejemplo de respuesta:

{
  "text": "María López tiene experiencia en Machine Learning, Python y TensorFlow. Carlos Gómez tiene habilidades en SQL, Machine Learning y Big Data."
}

Código de estado:

200 OK: Éxito.

400 Bad Request: Parámetros inválidos.

### Insertar nueva competencia

Registra una nueva competencia para un empleado.

URL: /insertar

Método: POST

Descripción: Extrae una competencia desde una frase proporcionada y la almacena en la base de datos.

Parámetros (JSON Request Body):

clave (string, requerido): Identificador único del empleado.

frase_competencia (string, requerido): Frase que describe la competencia adquirida.

Ejemplo de solicitud:

{
  "clave": "12345",
  "frase_competencia": "He trabajado en optimización de modelos de aprendizaje profundo con TensorFlow."
}

Ejemplo de respuesta:

{
  "mensaje": "Competencia agregada: Deep Learning Optimization. Respuesta de la IA: 'Optimización de modelos en TensorFlow permite mejorar la eficiencia y precisión de redes neuronales profundas.'"
}

Código de estado:

201 Created: Competencia agregada con éxito.

400 Bad Request: Datos inválidos o competencia ya registrada.

### Iniciar sesión

Verifica la identidad de un usuario.

URL: /sesion

Método: POST

Descripción: Comprueba si la clave de usuario existe en la base de datos y devuelve su nombre.

Parámetros (JSON Request Body):

clave (string, requerido): Clave del usuario para autenticación.

Ejemplo de solicitud:

{
  "clave": "12345"
}

Ejemplo de respuesta:

{
  "usuario": "Juan Pérez"
}

Si la clave no es válida:

{
  "usuario": "False"
}

Código de estado:

200 OK: Inicio de sesión exitoso.

401 Unauthorized: Clave incorrecta.

## Ejecución de la API

Para ejecutar la API localmente:

python server.py

La documentación interactiva generada por FastAPI está disponible en:

Swagger UI: http://localhost:8000/docs

Redoc: http://localhost:8000/redoc

## Tecnologías utilizadas

FastAPI: Framework para crear la API.

FAISS: Base de datos vectorial para búsqueda de competencias.

SQLite: Almacenamiento de datos.

Ollama (Llama3.2): Procesamiento de lenguaje natural.

HuggingFaceEmbeddings: Vectorización de competencias.

## Contacto y soporte

Si encuentras un problema, abre un issue en el repositorio de GitHub o contacta con el equipo de desarrollo (jorgeoteropailos@gmail.com).



