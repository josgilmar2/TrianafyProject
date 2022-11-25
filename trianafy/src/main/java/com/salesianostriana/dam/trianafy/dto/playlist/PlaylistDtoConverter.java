package com.salesianostriana.dam.trianafy.dto.playlist;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDtoConverter {

    public Playlist createPlaylistDtoToPlaylist(CreatePlaylistDto createPlaylistDto) {
        return Playlist.builder()
                .name(createPlaylistDto.getName())
                .description(createPlaylistDto.getDescription())
                .build();
    }

    public GetCreatedPlaylistDto playlistToGetCreatedPlaylistDto(Playlist playlist) {
        return GetCreatedPlaylistDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .build();
    }

    public GetPlaylistDto playlistToGetPlaylistDto(Playlist playlist) {
        return GetPlaylistDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .numberOfSongs(playlist.getSongs().size())
                .build();
    }

}
