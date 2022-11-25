package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.playlist.CreatePlaylistDto;
import com.salesianostriana.dam.trianafy.dto.playlist.GetCreatedPlaylistDto;
import com.salesianostriana.dam.trianafy.dto.playlist.GetPlaylistDto;
import com.salesianostriana.dam.trianafy.dto.playlist.PlaylistDtoConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
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

    @PostMapping("/")
    public ResponseEntity<GetCreatedPlaylistDto> createPlaylist(@RequestBody CreatePlaylistDto createPlaylistDto) {
        if(createPlaylistDto.getName() == null || createPlaylistDto.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }
        Playlist newPlaylist = playlistDtoConverter.createPlaylistDtoToPlaylist(createPlaylistDto);
        playlistService.add(newPlaylist);
        GetCreatedPlaylistDto response = playlistDtoConverter.playlistToGetCreatedPlaylistDto(newPlaylist);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findOnePlaylist(@PathVariable Long id) {
        return ResponseEntity.of(playlistService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPlaylistDto> editPlaylist(@PathVariable Long id,
                                                       @RequestBody CreatePlaylistDto editPlaylistDto) {
        if(playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if(editPlaylistDto.getName() == null || editPlaylistDto.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }else {
            Playlist editPlaylist = playlistDtoConverter.createPlaylistDtoToPlaylist(editPlaylistDto);
            return ResponseEntity.of(
                    playlistService.findById(id)
                            .map(old -> {
                                old.setName(editPlaylistDto.getName());
                                old.setDescription(editPlaylist.getDescription());
                                return Optional.of(playlistDtoConverter.playlistToGetPlaylistDto(old));
                            }).orElse(Optional.empty())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        if(playlistService.existsById(id))
            playlistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id1}/song/{id2}")
    public ResponseEntity<Playlist> addSongToAPlaylist(@PathVariable Long id1, @PathVariable Long id2) {
        if(playlistService.findById(id1).isEmpty() || songService.findById(id2).isEmpty()) {
            return ResponseEntity.notFound().build();
        //} else if(p.getName() == null || p.getDescription() == null) {
            //return ResponseEntity.badRequest().build();
        } else {
            Playlist playlist = playlistService.findById(id1).get();
            Song song = songService.findById(id2).get();
            playlistService.add(playlist).addSong(song);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(playlistService.add(playlist));
        }
    }

}
