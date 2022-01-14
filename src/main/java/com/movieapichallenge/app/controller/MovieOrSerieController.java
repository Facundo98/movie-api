package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.MovieOrSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/movieorseries")
public class MovieOrSerieController {

    @Autowired
    MovieOrSerieService movieOrSerieService;

    //Create a new movie or serie
    @PostMapping
    public ResponseEntity<?> create (@RequestParam(value = "movieOrSerie")String movieOrSerie, @RequestParam(value = "image")MultipartFile multipartFile){
        return movieOrSerieService.saveNewMovieOrSerie(movieOrSerie,multipartFile);
    }
}
