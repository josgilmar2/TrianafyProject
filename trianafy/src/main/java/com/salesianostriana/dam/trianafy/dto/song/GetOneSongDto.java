package com.salesianostriana.dam.trianafy.dto.song;

import com.salesianostriana.dam.trianafy.dto.artist.GetArtistDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOneSongDto {

    private Long id;
    private String title, album, year;
    private GetArtistDto artist;

}
