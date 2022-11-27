package com.salesianostriana.dam.trianafy.dto.song;

import com.salesianostriana.dam.trianafy.dto.artist.ArtistDtoConverter;
import com.salesianostriana.dam.trianafy.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SongDtoConverter {

    private final ArtistDtoConverter artistDtoConverter;

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

    public GetOneSongDto songToGetOneSongDto(Song song) {
        return GetOneSongDto.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(artistDtoConverter.artistToGetArtistDto(song.getArtist())
                        == null ? null : artistDtoConverter.artistToGetArtistDto(song.getArtist()))
                .album(song.getAlbum())
                .year(song.getYear())
                .build();
    }

}
