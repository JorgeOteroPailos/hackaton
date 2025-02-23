# Arquitectura de CompetenciApp

## Visión General
CompetenciApp es una aplicación cliente-servidor diseñada para gestionar competencias de trabajadores usando inteligencia artificial y procesamiento de lenguaje natural.

## Componentes Principales

### 1. Backend (Python - FastAPI)
- Servidor desarrollado en **FastAPI**.
- Base de datos en **SQLite**.
- Procesamiento de lenguaje natural con el modelo **Llama3.2**.
- Uso de **HuggingFaceEmbeddings** para vectorización de competencias.

### 2. Base de Datos (SQLite)
- Almacena información de los trabajadores, sus credenciales y competencias.
- Consultas y actualizaciones realizadas mediante `sqlite3`.

### 3. Cliente (Java - JavaFX)
- Aplicación de escritorio desarrollada en **JavaFX**.
- Interfaz gráfica con archivos `.fxml` para gestionar usuarios y competencias.
- Hojas de estilos en formato `.css` para que la interfaz resulte más atractiva al usuario.
- Comunicación con el backend vía HTTP usando peticiones POST.

## Flujos de Datos
- **Inicio de sesión:** El usuario introduce su clave y la aplicación consulta el servidor.
- **Consulta de trabajadores:** Se envía una solicitud con una palabra clave y se devuelve una lista de empleados con habilidades afines.
- **Registro de competencia:** Se analiza una frase para extraer una competencia y se almacena en la base de datos.


