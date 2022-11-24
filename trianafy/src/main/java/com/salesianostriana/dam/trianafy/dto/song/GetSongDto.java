package com.salesianostriana.dam.trianafy.dto.song;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSongDto {

    private Long id;
    private String title, artistName, album, year;

}
