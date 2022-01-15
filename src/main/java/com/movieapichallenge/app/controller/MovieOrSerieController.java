package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.MovieOrSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/movieorseries")
public class MovieOrSerieController {

    @Autowired
    MovieOrSerieService movieOrSerieService;

    //Create a new movie or serie
    @PostMapping
    public ResponseEntity<?> create (@RequestParam(value = "movieOrSerie")String movieOrSerie, @RequestParam(value = "image")MultipartFile multipartFile){
        return movieOrSerieService.create(movieOrSerie,multipartFile);
    }

    //Read a genre by id
    @GetMapping("/{id}")
    public ResponseEntity<?> read (@PathVariable(value = "id")Long movieOrSerieId){
        return movieOrSerieService.readById(movieOrSerieId);
    }

    //Read all genres
    @GetMapping
    public ResponseEntity<?> readAll(){
        return movieOrSerieService.readAll();
    }

    //Update a movie/serie
    @PutMapping("/{idMovieOrSerie}")
    public ResponseEntity<?> update (@RequestParam(value = "movieOrSerie")String movieOrSerie, @RequestParam(value = "image")MultipartFile multipartFile, @PathVariable(value = "idMovieOrSerie")Long idMovieOrSerie){
        return movieOrSerieService.update(movieOrSerie,multipartFile,idMovieOrSerie);
    }

    //Delete a movie/serie
    @DeleteMapping("/{idMovieOrSerie}")
    public ResponseEntity<?> delete(@PathVariable Long idMovieOrSerie){
        return movieOrSerieService.deleteById(idMovieOrSerie);
    }
}
