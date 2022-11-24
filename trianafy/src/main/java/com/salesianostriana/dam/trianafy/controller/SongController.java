package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.song.CreateSongDto;
import com.salesianostriana.dam.trianafy.dto.song.GetSongDto;
import com.salesianostriana.dam.trianafy.dto.song.SongDtoConverter;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
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

    @PostMapping("/")
    public ResponseEntity<Song> create(@RequestBody CreateSongDto createSongDto) {
        if (createSongDto.getArtistId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Song newSong = songDtoConverter.createSongDtoToSong(createSongDto);
        Artist artist = artistService.findById(createSongDto.getArtistId()).orElse(null);
        newSong.setArtist(artist);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(songService.add(newSong));
    }

    @GetMapping("/")
    public ResponseEntity<List<GetSongDto>> findAllSongs() {
        List<Song> data = songService.findAll();
        if(data.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GetSongDto> result =
                    data.stream()
                            .map(songDtoConverter::songToGetSongDto)
                            .collect(Collectors.toList());
            return ResponseEntity
                    .ok()
                    .body(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> findOneSong(@PathVariable Long id) {
        return ResponseEntity.of(songService.findById(id));
    }

    /*@PutMapping("{id}")
    public ResponseEntity<EditSongDto> editSong(@PathVariable Long id, @RequestBody EditSongDto) {

    }*/

}
