package com.salesianostriana.dam.trianafy.dto.playlist;

import com.salesianostriana.dam.trianafy.dto.song.SongDtoConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaylistDtoConverter {

    private final SongDtoConverter songDtoConverter;
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

    public GetPlaylistSongsDto playlistToGetPlaylistSongsDto(Playlist playlist) {
        return GetPlaylistSongsDto
                .builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .songs(playlist.getSongs().stream()
                        .map(songDtoConverter::songToGetSongDto)
                        .collect(Collectors.toList()))
                .build();
    }

}
