package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.song.CreateSongDto;
import com.salesianostriana.dam.trianafy.dto.song.GetOneSongDto;
import com.salesianostriana.dam.trianafy.dto.song.GetSongDto;
import com.salesianostriana.dam.trianafy.dto.song.SongDtoConverter;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final SongDtoConverter songDtoConverter;
    private final ArtistService artistService;

    @Operation(summary = "Creación de una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado una nueva canción con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSongDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 10,
                                                "title": "Moscow Mule",
                                                "artistName": "Bad Bunny",
                                                "album": "Un Verano Sin Ti",
                                                "year": "2022"
                                            }                                  
                                            """
                            )})}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado la canción debido a que alguno de " +
                            "los datos introducidos son erróneos",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<GetSongDto> createSong(@RequestBody CreateSongDto createSongDto) {
        if (createSongDto.getArtistId() == null || createSongDto.getTitle() == null
                || createSongDto.getAlbum() == null || createSongDto.getYear() == null
                || !artistService.existsById(createSongDto.getArtistId())) {
            return ResponseEntity.badRequest().build();
        }
        Song newSong = songDtoConverter.createSongDtoToSong(createSongDto);
        Artist artist = artistService.findById(createSongDto.getArtistId()).get();
        newSong.setArtist(artist);
        songService.add(newSong);
        GetSongDto response = songDtoConverter.songToGetSongDto(newSong);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Obtiene todas las canciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todas las canciones",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetSongDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                 {
                                                     "id": 4,
                                                     "title": "19 días y 500 noches",
                                                     "artistName": "Joaquín Sabina",
                                                     "album": "19 días y 500 noches",
                                                     "year": "1999"
                                                 },
                                                 {
                                                     "id": 5,
                                                     "title": "Donde habita el olvido",
                                                     "artistName": "Joaquín Sabina",
                                                     "album": "19 días y 500 noches",
                                                     "year": "1999"
                                                 },
                                                 {
                                                     "id": 6,
                                                     "title": "A mis cuarenta y diez",
                                                     "artistName": "Joaquín Sabina",
                                                     "album": "19 días y 500 noches",
                                                     "year": "1999"
                                                 },
                                                 {
                                                     "id": 7,
                                                     "title": "Don't Start Now",
                                                     "artistName": "Dua Lipa",
                                                     "album": "Future Nostalgia",
                                                     "year": "2019"
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
                                                     "id": 10,
                                                     "title": "Unforgiven",
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
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna canción",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<GetSongDto>> findAllSongs() {
        if(songService.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GetSongDto> response =
                    songService.findAll().stream()
                            .map(songDtoConverter::songToGetSongDto)
                            .collect(Collectors.toList());
            return ResponseEntity
                    .ok()
                    .body(response);
        }
    }

    @Operation(summary = "Obtiene una canción por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la canción indicada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetOneSongDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 4,
                                                "title": "19 días y 500 noches",
                                                "album": "19 días y 500 noches",
                                                "year": "1999",
                                                "artist": {
                                                    "id": 1,
                                                    "artist": "Joaquín Sabina"
                                                }
                                            }                                         
                                            """
                            )})}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la canción indicada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetOneSongDto> findOneSong(@Parameter(description = "Identificador de la canción a buscar")
                                                         @PathVariable Long id) {
        if(songService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songDtoConverter.songToGetOneSongDto(songService.findById(id).get()));
    }

    @Operation(summary = "Edita una canción por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado la canción con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSongDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 4,
                                                "title": "Moscow Mule",
                                                "artistName": "Bad Bunny",
                                                "album": "Un Verano Sin Ti",
                                                "year": "2022"
                                            }                                        
                                            """
                            )})}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha editado la canción debido a que alguno de " +
                            "los datos introducidos son erróneos",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la canción indicada",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetSongDto> editSong(@Parameter(description = "Identificador de la canción a editar")
                                                   @PathVariable Long id, @RequestBody CreateSongDto editSongDto) {
        if(songService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if(editSongDto.getArtistId() == null || editSongDto.getTitle() == null
                || editSongDto.getAlbum() == null || editSongDto.getYear() == null
                || !artistService.existsById(editSongDto.getArtistId())) {
            return ResponseEntity.badRequest().build();
        } else {
            Song editSong = songDtoConverter.createSongDtoToSong(editSongDto);
            Artist artist = artistService.findById(editSongDto.getArtistId()).get();
            return ResponseEntity.of(
                    songService.findById(id)
                            .map(old -> {
                                old.setTitle(editSong.getTitle());
                                old.setAlbum(editSong.getAlbum());
                                old.setYear(editSong.getYear());
                                old.setArtist(artist);
                                return Optional.of(songDtoConverter.songToGetSongDto(old));
                            }).orElse(Optional.empty())
            );
        }
    }

    @Operation(summary = "Elimina una canción por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente la canción indicada",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@Parameter(description = "Identificador de la canción a eliminar")
                                            @PathVariable Long id) {
        if(songService.existsById(id))
           songService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
