package com.salesianostriana.dam.trianafy.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPlaylistDto {

    private Long id;
    private String name;
    private int numberOfSongs;

}
