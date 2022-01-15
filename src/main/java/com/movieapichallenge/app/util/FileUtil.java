package com.movieapichallenge.app.util;

import lombok.*;

import java.nio.file.Path;
import java.nio.file.Paths;


@Getter
public class FileUtil {

    private final String characterImageStoragePath = System.getProperty("user.dir") + "/src/main/resources/uploads/characterImage/";
    private final String movieOrSerieImageStoragePath = System.getProperty("user.dir") + "/src/main/resources/uploads/movieOrSerieImage/";
    private final String genreImageStoragePath = System.getProperty("user.dir") + "/src/main/resources/uploads/genreImage/";

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
