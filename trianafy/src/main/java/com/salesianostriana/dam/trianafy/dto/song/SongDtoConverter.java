package com.salesianostriana.dam.trianafy.dto.song;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongDtoConverter {

    public Song createSongDtoToSong(CreateSongDto createSongDto) {
        return Song.builder()
                .title(createSongDto.getTitle())
                .album(createSongDto.getAlbum())
                .year(createSongDto.getYear())
                .build();
    }

    public GetSongDto songToGetSongDto(Song song) {
        return GetSongDto.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artistName(song.getArtist() == null ? null : song.getArtist().getName())
                .album(song.getAlbum())
                .year(song.getYear())
                .build();
    }

}
