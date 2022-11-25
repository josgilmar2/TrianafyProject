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
    public ResponseEntity<GetSongDto> createSong(@RequestBody CreateSongDto createSongDto) {
        if (createSongDto.getArtistId() == null || createSongDto.getTitle() == null
                || createSongDto.getAlbum() == null || createSongDto.getYear() == null) {
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

    @GetMapping("/{id}")
    public ResponseEntity<Song> findOneSong(@PathVariable Long id) {
        return ResponseEntity.of(songService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetSongDto> editSong(@PathVariable Long id, @RequestBody CreateSongDto editSongDto) {
        if(songService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if(editSongDto.getArtistId() == null || editSongDto.getTitle() == null
                || editSongDto.getAlbum() == null || editSongDto.getYear() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            Song editSong = songDtoConverter.createSongDtoToSong(editSongDto);
            Artist artist = artistService.findById(editSongDto.getArtistId()).orElse(null);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        if(songService.existsById(id))
           songService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
