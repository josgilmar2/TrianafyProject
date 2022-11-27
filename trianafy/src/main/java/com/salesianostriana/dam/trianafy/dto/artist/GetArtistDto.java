package com.salesianostriana.dam.trianafy.dto.artist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetArtistDto {

    private Long id;
    private String artist;

}
