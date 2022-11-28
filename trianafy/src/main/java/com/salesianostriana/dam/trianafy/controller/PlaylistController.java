package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.playlist.*;
import com.salesianostriana.dam.trianafy.dto.song.GetOneSongDto;
import com.salesianostriana.dam.trianafy.dto.song.SongDtoConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistDtoConverter playlistDtoConverter;
    private final SongService songService;
    private final SongDtoConverter songDtoConverter;

    @Operation(summary = "Creación de una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado una nueva playlist con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCreatedPlaylistDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 13,
                                                "name": "Reggaeton",
                                                "description": "Playlist de reggaeton"
                                            }                                  
                                            """
                            )})}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado la playlist debido a que alguno de " +
                            "los datos introducidos son erróneos",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<GetCreatedPlaylistDto> createPlaylist(@RequestBody CreatePlaylistDto createPlaylistDto) {
        if (createPlaylistDto.getName() == null || createPlaylistDto.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }
        Playlist newPlaylist = playlistDtoConverter.createPlaylistDtoToPlaylist(createPlaylistDto);
        playlistService.add(newPlaylist);
        GetCreatedPlaylistDto response = playlistDtoConverter.playlistToGetCreatedPlaylistDto(newPlaylist);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Obtiene todas las playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todas las playlists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                  {
                                                      "id": 12,
                                                      "name": "Random",
                                                      "numberOfSongs": 4
                                                  }
                                            ]                                          
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna playlist",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<GetPlaylistDto>> findAllPlaylists() {
        if (playlistService.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GetPlaylistDto> response =
                    playlistService.findAll().stream()
                            .map(playlistDtoConverter::playlistToGetPlaylistDto)
                            .collect(Collectors.toList());
            return ResponseEntity.ok()
                    .body(response);
        }
    }

    @Operation(summary = "Obtiene una playlist por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la playlist indicada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistSongsDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 12,
                                                "name": "Random",
                                                "description": "Una lista muy loca",
                                                "songs": [
                                                    {
                                                        "id": 9,
                                                        "title": "Enter Sandman",
                                                        "artistName": "Metallica",
                                                        "album": "Metallica",
                                                        "year": "1991"
                                                    },
                                                    {
                                                        "id": 8,
                                                        "title": "Love Again",
                                                        "artistName": "Dua Lipa",
                                                        "album": "Future Nostalgia",
                                                        "year": "2021"
                                                    },
                                                    {
                                                        "id": 9,
                                                        "title": "Enter Sandman",
                                                        "artistName": "Metallica",
                                                        "album": "Metallica",
                                                        "year": "1991"
                                                    },
                                                    {
                                                        "id": 11,
                                                        "title": "Nothing Else Matters",
                                                        "artistName": "Metallica",
                                                        "album": "Metallica",
                                                        "year": "1991"
                                                    }
                                                ]
                                            }                                         
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la playlist indicada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetPlaylistSongsDto> findOnePlaylist(@Parameter(description = "Identificador de la playlist a buscar")
                                                               @PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playlistDtoConverter.playlistToGetPlaylistSongsDto(playlistService.findById(id).get()));
    }

    @Operation(summary = "Edita una playlist por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado la playlist con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 13,
                                                "name": "Reggaeton",
                                                "description": "Playlist de Reggaeton"
                                            }                                        
                                            """
                            )})}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha editado la playlist debido a que alguno de " +
                            "los datos introducidos son erróneos",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la playlist indicada",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetPlaylistDto> editPlaylist(@Parameter(description = "Identificador de la playlist a editar")
                                                       @PathVariable Long id,
                                                       @RequestBody CreatePlaylistDto editPlaylistDto) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (editPlaylistDto.getName() == null || editPlaylistDto.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            Playlist editPlaylist = playlistDtoConverter.createPlaylistDtoToPlaylist(editPlaylistDto);
            return ResponseEntity.of(
                    playlistService.findById(id)
                            .map(old -> {
                                old.setName(editPlaylist.getName());
                                old.setDescription(editPlaylist.getDescription());
                                return Optional.of(playlistDtoConverter.playlistToGetPlaylistDto(old));
                            }).orElse(Optional.empty())
            );
        }
    }

    @Operation(summary = "Elimina una playlist por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente la playlist indicada",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@Parameter(description = "Identificador de la playlist a eliminar")
                                            @PathVariable Long id) {
        if (playlistService.existsById(id))
            playlistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adición de una canción a una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido una nueva canción dentro de la playlist con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetPlaylistSongsDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 12,
                                                "name": "Random",
                                                "description": "Una lista muy loca",
                                                "songs": [
                                                    {
                                                        "id": 9,
                                                        "title": "Enter Sandman",
                                                        "artistName": "Metallica",
                                                        "album": "Metallica",
                                                        "year": "1991"
                                                    },
                                                    {
                                                        "id": 8,
                                                        "title": "Love Again",
                                                        "artistName": "Dua Lipa",
                                                        "album": "Future Nostalgia",
                                                        "year": "2021"
                                                    },
                                                    {
                                                        "id": 9,
                                                        "title": "Enter Sandman",
                                                        "artistName": "Metallica",
                                                        "album": "Metallica",
                                                        "year": "1991"
                                                    },
                                                    {
                                                        "id": 11,
                                                        "title": "Nothing Else Matters",
                                                        "artistName": "Metallica",
                                                        "album": "Metallica",
                                                        "year": "1991"
                                                    },
                                                    {
                                                        "id": 5,
                                                        "title": "Donde habita el olvido",
                                                        "artistName": "Joaquín Sabina",
                                                        "album": "19 días y 500 noches",
                                                        "year": "1999"
                                                    }
                                                ]
                                            }                                  
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado la playlist o la canción indicadas",
                    content = @Content)
    })
    @PostMapping("/{id1}/song/{id2}")
    public ResponseEntity<GetPlaylistSongsDto> addSongToAPlaylist(@Parameter(description = "Identificador de la playlist a la que se le quiere añadir la canción")
                                                                  @PathVariable Long id1,
                                                                  @Parameter(description = "Identificador de la canción a añadir")
                                                                  @PathVariable Long id2) {
        if (playlistService.findById(id1).isEmpty() || songService.findById(id2).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Playlist playlist = playlistService.findById(id1).get();
            Song song = songService.findById(id2).get();
            playlistService.add(playlist).addSong(song);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(playlistDtoConverter.playlistToGetPlaylistSongsDto(playlistService.add(playlist)));
        }
    }

    @Operation(summary = "Obtiene las canciones de una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las canciones de la playlist indicada",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetPlaylistSongsDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 12,
                                                    "name": "Random",
                                                    "description": "Una lista muy loca",
                                                    "songs": [
                                                        {
                                                            "id": 9,
                                                            "title": "Enter Sandman",
                                                            "artistName": "Metallica",
                                                            "album": "Metallica",
                                                            "year": "1991"
                                                        },
                                                        {
                                                            "id": 8,
                                                            "title": "Love Again",
                                                            "artistName": "Dua Lipa",
                                                            "album": "Future Nostalgia",
                                                            "year": "2021"
                                                        },
                                                        {
                                                            "id": 9,
                                                            "title": "Enter Sandman",
                                                            "artistName": "Metallica",
                                                            "album": "Metallica",
                                                            "year": "1991"
                                                        },
                                                        {
                                                            "id": 11,
                                                            "title": "Nothing Else Matters",
                                                            "artistName": "Metallica",
                                                            "album": "Metallica",
                                                            "year": "1991"
                                                        },
                                                        {
                                                            "id": 5,
                                                            "title": "Donde habita el olvido",
                                                            "artistName": "Joaquín Sabina",
                                                            "album": "19 días y 500 noches",
                                                            "year": "1999"
                                                        }
                                                    ]
                                                }
                                            ]                                         
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la playlist indicada",
                    content = @Content)
    })
    @GetMapping("/{id}/song")
    public ResponseEntity<List<GetPlaylistSongsDto>> getSongsOfAPlaylist(@Parameter(description = "Identificador de la playlist")
                                                                         @PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GetPlaylistSongsDto> response =
                    playlistService.findAll().stream()
                            .map(playlistDtoConverter::playlistToGetPlaylistSongsDto)
                            .collect(Collectors.toList());
            return ResponseEntity.ok()
                    .body(response);
        }
    }

    @Operation(summary = "Obtiene una canción de una playlist concreta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la canción de la playlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetOneSongDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                 "id": 9,
                                                 "title": "Enter Sandman",
                                                 "album": "Metallica",
                                                 "year": "1991",
                                                 "artist": {
                                                     "id": 3,
                                                     "artist": "Metallica"
                                                 }
                                            }                                         
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado la playlist o la canción indicadas",
                    content = @Content)
    })
    @GetMapping("/{id1}/song/{id2}")
    public ResponseEntity<GetOneSongDto> findOneSongOfAPlaylist(@Parameter(description = "Identificador de la playlist a la que se le quiere buscar la canción")
                                                                @PathVariable Long id1,
                                                                @Parameter(description = "Identificador de la canción a buscar")
                                                                @PathVariable Long id2) {
        Playlist playlist = playlistService.findById(id1).get();
        if (songService.findById(id2).isPresent() && playlist.getSongs().contains(songService.findById(id2).get())) {
            return ResponseEntity.ok(songDtoConverter.songToGetOneSongDto(songService.findById(id2).get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una canción de una playlist concreta",
            description = "Si existen dos o más canciones iguales en la misma playlist, se eliminan todas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente la canción de la playlist",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado la playlist o la canción indicadas",
                    content = @Content)
    })
    @DeleteMapping("/{id1}/song/{id2}")
    public ResponseEntity<?> deleteSongFromAPlaylist(@Parameter(description = "Identificador de la playlist a la que se le quiere borrar la canción")
                                                     @PathVariable Long id1,
                                                     @Parameter(description = "Identificador de la canción a borrar")
                                                     @PathVariable Long id2) {
        if (playlistService.findById(id1).isEmpty() || songService.findById(id2).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Playlist playlist = playlistService.findById(id1).get();
        while (playlist.getSongs().contains(songService.findById(id2).get())) {
            playlist.deleteSong(songService.findById(id2).get());
        }
        playlistService.edit(playlistService.findById(id1).get());
        return ResponseEntity.noContent().build();
    }

}
