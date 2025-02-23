# Contribuir a CompetenciApp

¡Gracias por tu interés en contribuir a CompetenciApp! Este proyecto de código abierto busca mejorar la gestión de competencias en empresas mediante inteligencia artificial y procesamiento de lenguaje natural.

## 🛠️ Requisitos previos
Antes de contribuir, asegúrate de tener instalados:

- **Python 3.8 o superior** con las dependencias del backend (`pip install -r requirements.txt`).
- **Java 17 o superior** con Gradle (`./gradlew build`).
- **SQLite** para manejar la base de datos.
- **Conocimientos en FastAPI, JavaFX y HuggingFaceEmbeddings** (opcional pero recomendado).

## 📌 Cómo contribuir

### 1️⃣ Forkea el repositorio
Haz un fork del proyecto en GitHub y clona tu copia localmente:

```sh
git clone https://github.com/JorgeOteroPailos/hackaton
cd competencIApp
```
### 2️⃣ Crea una rama para tu contribución
Utiliza un nombre descriptivo para tu rama:

```sh
Copy
Edit
git checkout -b feature-nombre-mejora
```
### 3️⃣ Realiza los cambios y asegúrate de que el código funcione
Antes de hacer un commit, asegúrate de que la aplicación sigue funcionando y de que tus cambios no rompen ninguna funcionalidad:

sh
Copy
Edit
# Prueba el backend
python server.py

# Prueba la interfaz en JavaFX
./gradlew run
### 4️⃣ Sigue las normas de estilo
Python: Sigue la guía de estilo PEP 8.
Java: Usa convenciones estándar y formatea el código con ./gradlew format.
Commits: Usa mensajes claros y estructurados, por ejemplo:
sh
Copy
Edit
git commit -m "feat: agregada funcionalidad de consulta de competencias"
### 5️⃣ Envía un Pull Request (PR)
Sube tus cambios a tu repositorio y crea un PR hacia la rama main:

sh
Copy
Edit
git push origin feature-nombre-mejora
En la descripción del PR, explica los cambios realizados y menciona si hay problemas o puntos a mejorar.
