package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.MovieOrSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movieorseries")
public class MovieOrSerieController {

    @Autowired
    MovieOrSerieService movieOrSerieService;

    //Create a new movie or serie
    @PostMapping
    public ResponseEntity<?> create (@RequestParam(value = "movieOrSerie")String movieOrSerie, @RequestParam(value = "image")MultipartFile multipartFile){
        return movieOrSerieService.create(movieOrSerie,multipartFile);
    }

    //Add character to the movie/serie
    @PostMapping("/{movieOrSerieId}/characters/{characterId}")
    public ResponseEntity<?> enrollCharacterToMovieOrSerie(@PathVariable Long movieOrSerieId, @PathVariable Long characterId){
        return movieOrSerieService.enrollCharacter(movieOrSerieId,characterId);
    }

    //Read a genre by id
    @GetMapping()
    public ResponseEntity<?> readAll(){
        return movieOrSerieService.readAll();
    }

    //Read a movie/serie by the tittle
    @GetMapping(params = {"tittle"})
    public ResponseEntity<?> readByTittle(@RequestParam(value = "tittle")String tittle,@RequestParam(value = "order",required = false)String order){
        return movieOrSerieService.readByTittle(tittle,order);
    }

    //Read a movie/serie by the tittle and genre id
    @GetMapping(params = {"tittle","genre"})
    public ResponseEntity<?> readByTittleAndGenreId(@RequestParam(value = "tittle")String tittle,@RequestParam(value = "genre")Long genreId,@RequestParam(value = "order",required = false)String order){
        return movieOrSerieService.readByTittleAndGenre(tittle,genreId,order);
    }

    //Update a movie/serie
    @PutMapping("/{idMovieOrSerie}")
    public ResponseEntity<?> update (@RequestParam(value = "movieOrSerie")String movieOrSerie, @RequestParam(value = "image")MultipartFile multipartFile, @PathVariable(value = "idMovieOrSerie")Long MovieOrSerieId){
        return movieOrSerieService.update(movieOrSerie,multipartFile,MovieOrSerieId);
    }

    //Delete a movie/serie
    @DeleteMapping("/{idMovieOrSerie}")
    public ResponseEntity<?> delete(@PathVariable Long MovieOrSerieId){
        return movieOrSerieService.deleteById(MovieOrSerieId);
    }

    //Delete an enroled character
    @DeleteMapping("/{movieOrSerieId}/characters/{characterId}")
    public ResponseEntity<?> deleteEnrolledCharacter(@PathVariable Long movieOrSerieId, @PathVariable Long characterId){
        return movieOrSerieService.deleteCharacter(movieOrSerieId,characterId);
    }
}
