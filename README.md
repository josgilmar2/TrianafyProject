# Trianafy
<p align="center">
  <img src="https://img.shields.io/badge/STATUS-FINISH-red" alt="Status del proyecto"/>
  <a href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html"><img src="https://img.shields.io/badge/jdk-v17.0.4.1-blue" alt="Versión java" /></a>
  <a href="https://maven.apache.org/download.cgi"><img src="https://img.shields.io/badge/apache--maven-v3.8.6-blue" alt="Versión maven" /></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/spring--boot-v2.7.5-blue" alt="Versión spring-boot" /></a>
  <img src="https://img.shields.io/badge/release%20date-november-yellowgreen" alt="Lanzamiento del proyecto" />
  <img src="https://img.shields.io/badge/license-MIT-brightgreen" alt="Licencia" />
</p>
</br>
## Descripción
**Trianafy** es una aplicación desarrollada con Spring Boot 2.7.5 que nos permite gestionar listas de reproducción
de música 
</br>
## Autor
#### José Luis Gil Martín
</br>

### Entidades

* #### Artist
  - Artist (entity/model)
  - ArtistService
  - ArtistRepository
  - ArtistController
  - ArtistDTOs

* #### Song
  - Song (entity/model)
  - SongService
  - SongRepository
  - SongController
  - SongDTOs

* #### Playlist
  - Playlist (entitiy/model)
  - PlaylistService
  - PlaylistRepository
  - PlaylistController
  - PlaylistDTOs
</br>

## :hammer:Funcionalidades

### Funcionalidades de Artist

1. Añadir un artista
2. Obtener todos los artistas
3. Obtener un artista por su identificador
4. Editar un artista por su identificador
5. Borrar un artista por su identificador (no se borran sus canciones)

### Funcionalidades de Song

1. Añadir una canción
2. Obtener todas las canciones
3. Obtener un artista por su identificador
4. Editar una canción por su identificador
5. Borrar una canción por su identificador

### Funcionalidades de Playlist

1. Añadir una playlist
2. Obtener todas las playlists
3. Obtener una playlist por su identificador
4. Editar una playlist por su identificador
5. Borrar una playlist por su identificador
6. Añadir una canción existente a una playlist
7. Obtener todas las canciones de una playlist
8. Obtener los datos de una canción en concreto de una playlist
9. Borrar una canción de una playlist

## Arrancar el proyecto

* Descargar IntelliJ IDEA Community Edition
* Descargar Java jdk 17
* * Descargar apache-maven 3.8.6
* Configurar la versón de Java dentro de IntelliJ
* Clicar en el botón *Edit Configurations*
* Añadir una nueva configuración de tipo **Maven**
* En el apartado de *Run* escribir **spring-boot:run**
