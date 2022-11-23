package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService service;

    @PostMapping("/")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist a) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.add(a));
    }

    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAllArtists() {
        if (service.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findOneArtist(@PathVariable Long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> editArtist(@PathVariable Long id, @RequestBody Artist a) {
        if(service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.of(
                    service.findById(id)
                            .map(old -> {
                                old.setName(a.getName());
                                return service.edit(old);
                            })
            );
        }
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable Long id) {
        if(!service.findById(id).isEmpty())
            service.deleteById(id);
        return ResponseEntity.noContent().build();
    }*/

}
