package com.movieapichallenge.app.util;

import lombok.*;


@Getter
public class FileUtil {

    private String characterImagePath = System.getProperty("user.dir") + "/src/main/resources/uploads/characterImage/";
    private String movieOrSerieImagePath = System.getProperty("user.dir") + "/src/main/resources/uploads/movieOrSerieImage/";
    private String genreImagePath = System.getProperty("user.dir") + "/src/main/resources/uploads/genreImage/";
}
