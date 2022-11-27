package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;
    private final SongService songService;

    @Operation(summary = "Creación de un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado un nuevo artista con éxito",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 4,
                                                "name": "Bad Bunny"
                                            }                                  
                                            """
                            )})}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado el artista debido a que alguno de " +
                            "los datos introducidos son erróneos",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist a) {
        if (a.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(artistService.add(a));
    }

    @Operation(summary = "Obtiene todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todos los artistas",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 1,
                                                    "name": "Joaquín Sabina"
                                                },
                                                {
                                                    "id": 2,
                                                    "name": "Dua Lipa"
                                                },
                                                {
                                                    "id": 3,
                                                    "name": "Metallica"
                                                }
                                            ]                                          
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún artista",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAllArtists() {
        if (artistService.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(artistService.findAll());
        }
    }

    @Operation(summary = "Obtiene un artista por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado el artista indicado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 2,
                                                "name": "Dua Lipa"
                                            }                                          
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado al artista indicado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findOneArtist(@Parameter(description = "Identificador del artista a buscar")
                                                    @PathVariable Long id) {
        return ResponseEntity.of(artistService.findById(id));
    }

    @Operation(summary = "Edita un artista por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado el artista con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 3,
                                                "name": "Ozuna"
                                            }                                          
                                            """
                            )})}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha editado el artista debido a que alguno de " +
                            "los datos introducidos son erróneos",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado al artista indicado",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Artist> editArtist(@Parameter(description = "Identificador del artista a editar")
                                                 @PathVariable Long id, @RequestBody Artist a) {
        if (artistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (a.getName() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.of(
                    artistService.findById(id)
                            .map(old -> {
                                old.setName(a.getName());
                                return artistService.edit(old);
                            })
            );
        }
    }

    @Operation(summary = "Elimina un artista por su identificador",
            description = "Al borrar un artista, no se eliminan las canciones de dicho " +
                    "artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente el artista indicado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@Parameter(description = "Identificador del artista a eliminar")
                                            @PathVariable Long id) {
        if (artistService.existsById(id)) {
            songService.findByArtistId(id).stream()
                    .forEach(s -> s.setArtist(null));
            artistService.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

}
