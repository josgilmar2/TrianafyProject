package com.salesianostriana.dam.trianafy;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitData {

    private final ArtistService artistService;
    private final SongService songService;

    @PostConstruct
    public void initData() {
        Artist first = new Artist();
        first.setName("Bad Bunny");
        artistService.add(first);

        Artist second = new Artist();
        second.setName("Ozuna");
        artistService.add(second);

        Artist third = new Artist();
        third.setName("Feid");

        Song firstSong = new Song();
        firstSong.setTitle("Moscow Mule");
        firstSong.setAlbum("Un Verano Sin Ti");
        firstSong.setYear("2022");
        firstSong.setArtist(first);
        songService.add(firstSong);

        Song secondSong = new Song();
        secondSong.setTitle("Neverita");
        secondSong.setAlbum("Un Verano Sin Ti");
        secondSong.setYear("2022");
        secondSong.setArtist(first);
        songService.add(secondSong);
    }

}
