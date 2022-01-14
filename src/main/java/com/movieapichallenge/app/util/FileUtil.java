package com.movieapichallenge.app.util;

import lombok.*;

import java.nio.file.Path;
import java.nio.file.Paths;


@Getter
public class FileUtil {

    private String characterImageStoragePath = System.getProperty("user.dir") + "/src/main/resources/uploads/characterImage/";
    private String movieOrSerieImageStoragePath = System.getProperty("user.dir") + "/src/main/resources/uploads/movieOrSerieImage/";
    private String genreImageStoragePath = System.getProperty("user.dir") + "/src/main/resources/uploads/genreImage/";

    private Path characterImagePath = Paths.get(characterImageStoragePath);
    private Path movieOrSeriePath = Paths.get(movieOrSerieImageStoragePath);
    private Path genrePath = Paths.get(genreImageStoragePath);

    public Path getImagePath(Long characterId, String fileName){
        return Paths.get(characterImageStoragePath + characterId + "/" + fileName);
    }

    public Path getGenrePath(Long genreId, String fileName){
        return Paths.get(genreImageStoragePath + genreId + "/" + fileName);
    }

    public Path getMovieOrSeriePath(Long movieOrSerieId, String fileName){
        return Paths.get(movieOrSerieImageStoragePath + movieOrSerieId + "/" + fileName);
    }

}
