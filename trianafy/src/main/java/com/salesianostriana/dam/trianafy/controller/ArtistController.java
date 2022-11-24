package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;
    private final SongService songService;

    @PostMapping("/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist a) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(artistService.add(a));
    }

    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAllArtists() {
        if (artistService.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(artistService.findAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findOneArtist(@PathVariable Long id) {
        return ResponseEntity.of(artistService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> editArtist(@PathVariable Long id, @RequestBody Artist a) {
        if(artistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable Long id) {
        if(artistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<Song> songsOfAnArtist = songService.findByArtistId(id);
            List<Song>songsFilter = songsOfAnArtist.stream()
                    .map(c -> {
                        c.setArtist(null);
                        return c;
                    }).collect(Collectors.toList());
            songService.addAll(songsFilter);
            artistService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

    }

}
