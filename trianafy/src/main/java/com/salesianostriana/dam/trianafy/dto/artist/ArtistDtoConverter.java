package com.salesianostriana.dam.trianafy.dto.artist;

import com.salesianostriana.dam.trianafy.model.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistDtoConverter {

    public GetArtistDto artistToGetArtistDto(Artist artist) {
        if(artist == null) {
            return GetArtistDto
                    .builder()
                    .id(null)
                    .artist(null)
                    .build();
        }
        return GetArtistDto
                .builder()
                .id(artist.getId())
                .artist(artist.getName())
                .build();
    }
}
