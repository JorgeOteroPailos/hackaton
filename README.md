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

## Sobre el desarrollo del proyecto

### Inspiration

Este proyecto fue inspirado por el reto propuesto por Gradiant en el HackUDC 2025, donde se sugería el siguiente problema: en una empresa, cuando una parte del equipo trabaja con una tecnología, adquiere muchos conocimientos que podrían ser de utilidad futura para sus compañeros. De ahí surge la necesidad de registrar y manejar dicha información, de alguna manera cómoda para los usuarios que la inserten (para que no eviten hacerlo) y para los usuarios que la consulten.

### What it does

El proyecto desarrollado almacena, a partir de sencillos prompts en lenguaje natural, las competencias que adquiere cada uno de sus usuarios. De igual manera, tras un prompt en lenguaje natural para la consulta, informa al usuario de otros trabajadores que puedan ser competentes en la disciplina que corresponda. 

Internamente, transforma la parte "relevante" de cada prompt en embeddings que puede almacenar y comparar para encontrar similitudes entre competencias semánticamente parecidas (por ejemplo, relacionar a un usuario que maneja "MPI" con uno que busca "programación paralela") y aportar un servicio de utilidad de una manera accesible y user-friendly.

### Challenges we ran into

El primer gran reto del proyecto fue decidir, conceptualmente, los sistemas que utilizaríamos para el registro y la gestión de la organización. Tras llegar a la conclusión de que utilizando LLMs para la comparación de embeddings para encontrar competencias semánticamente similares (de manera similar a cómo lo haría un sistema RAG) podría proporcionarnos grandes resultados, a la vez que evitaba la necesidad de que el usuario rellenase horribles formularios que pronto quedarían obsoletos. 

No obstante, nuestro conocimiento acerca de la integración de LLMs en aplicaciones requirió una exhaustiva investigación al respecto, además de sobre cómo usar dependencias como Ollama o Langchain. El propio uso de la IA ya integrada en la aplicación también fue todo un reto, así como comprender profundamente cómo funcionaba la generación y la búsqueda de cercanía en embeddings.

Debido a que no teníamos conocimiento acerca de interfaces gráficas en Python, decidimos crear el programa cliente en Java utlizando javafx. Aunque esto también trajo sus propios retos y dificultades, las más importantes fueron las derivadas de la comunicación entre las dos partes del programa: el servidor en Python y el Cliente en java. Aprender a usar http fue un reto, pero superado de manera exitosa.

### Accomplishments that we're proud of

En primer lugar, haber podido sacar este proyecto adelante en tan poco tiempo en las condiciones del momento, superando cualquier reto que se nos pusiese por delante y adaptándonos a nuestra necesidades y a nuestras circunstancias es en sí una razón para estar orgullosos. No obstante, nuestro principal motivo de orgullo es el Software que desarrollamos. CompetencIApp es una aplicación realmente útil y con potencial para cualquier organización, que además utiliza tecnologías innovadoras para ello. Por último, consideramos que hacer Software libre a disponibilidad de cualquiera que le encuentre utilidad es algo beneficioso para la sociedad y un motivo más de orgullo, en lo que al proyecto se refiere.

### What we learned

Tras el desarrollo de un proyecto así y en unas circunstancias tan especiales (HackUDC2025), uno aprende una gran cantidad de cosas realmente importantes. Si bien no directamente relacionadas con el proyecto, su desarrollo nos ha brindado una gran oportunidad de aprendizaje en disciplinas transversales vitales como la resolución de problemas, el trabajo en equipo, la gestión de proyectos, el diseño de Software, la priorización de requisitos, etc.

No obstante, el propio proyecto, que, como mencionamos anteriormente, cuenta con muchas tecnologías con las que antes de este desarrollo no estábamos familiarizados, también nos ha brindado la oportunidad (hasta cierto punto, nos ha forzado a) de aprender a utilizar un gran número de assets como LLMs, su integración en el programa, Ollama, Langchain, SQLite, http, búsqueda por comparación de embeddings... por mencionar algunas de las que más ajenas nos resultaban hace unos pocos días.

### What's next for CompetenciApp

Si bien no dudamos de la completitud de nuestro código, somos conscientes de que las circunstancias en que este ha sido desarrollado lo dotan de una naturaleza (aún) burda y que requiere de ser pulida, completada y mejorada, para lo cual contamos tanto con nosotros mismos como con cualquier interesado en el proyecto. 

Si hablamos de funcionalidades completas, nos hubiese gustado permitir la inserción de documentos para que pudiesen procesarse de manera similar a cómo se procesan ahora las competencias de los compañeros, o la integración con las APIs de Linkedin o Github.

