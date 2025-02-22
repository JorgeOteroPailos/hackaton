# CompetenciApp

**CompetenciApp** es una aplicación diseñada para gestionar y consultar competencias de empleados en una empresa utilizando procesamiento de lenguaje natural e inteligencia artificial. Cuando un empleado o grupo de empleados trabajan con una tecnología, estos aprenden sobre ella, y podrán ser de utilidad para trabajar con ella en el futuro. CompetenciApp utiliza herramientas basadas en la Inteligencia Artificial para facilitar el registro y la posterior localización de dichas experiencias útiles, evitando que el usuario no registre sus experiencias por falta de una buena accesibilidad.

## Características principales

- **Base de datos SQLite** para almacenamiento de información de empleados.
- **Procesamiento de lenguaje natural** con el modelo `llama3.2` para extraer competencias de frases.
- **Vectorización de competencias** usando `HuggingFaceEmbeddings`.
- **Backend con FastAPI** para gestionar las solicitudes de la aplicación.
- **Interfaz gráfica con JavaFX** para interacción con los usuarios.

## Instalación y ejecución

### Requisitos previos

- Python 3.8 o superior
- Java 17 o superior
- Gradle (incluido en el repositorio)
- Dependencias de Python y Java

### Instalación

1. Clona el repositorio:
   ```sh
   git clone https://github.com/hackudc/competenciapp.git
   cd competenciapp
   ```
2. Instala las dependencias de Python:
   ```sh
   pip install -r requirements.txt
   ```
3. Ejecuta el backend:
   ```sh
   python server.py
   ```
4. Compila y ejecuta la aplicación Java:
   ```sh
   ./gradlew run
   ```

## Uso de la API

La API proporciona los siguientes endpoints:

- **POST `/consultar`** – Busca trabajadores con conocimientos sobre un tema específico.
- **POST `/insertar`** – Agrega una competencia a un trabajador.
- **POST `/sesion`** – Verifica la identidad de un usuario.

Consulta [API_DOCS.md](API_DOCS.md) para más detalles.

## Arquitectura

La aplicación sigue una arquitectura cliente-servidor:

- **Backend en Python** con FastAPI y SQLite.
- **Procesamiento de texto con Llama3.2 y embeddings**
- **Cliente en Java** con JavaFX.

Más información en [ARCHITECTURE.md](ARCHITECTURE.md).

## Contribución

Si deseas contribuir, revisa las pautas en [CONTRIBUTING.md](CONTRIBUTING.md).

## Licencia

Este proyecto está licenciado bajo los términos de la licencia incluida en el archivo `LICENSE`.

