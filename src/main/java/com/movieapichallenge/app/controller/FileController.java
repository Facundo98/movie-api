package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    //Download the respective character image
    @GetMapping("/api/files/characterImage/{idCharacter}/{fileName}")
    public ResponseEntity downloadCharacterImage(@PathVariable( value = "idCharacter") Long idCharacter, @PathVariable(value = "fileName") String fileName, HttpServletRequest request){

        return fileService.downloadFile(idCharacter,fileName,request,"character");
    }

    //Download the respective genre image
    @GetMapping("/api/files/genreImage/{idGenre}/{fileName}")
    public ResponseEntity downloadGenreImage(@PathVariable(value = "idGenre")Long idGenre, @PathVariable(value = "fileName") String fileName, HttpServletRequest request){
        return fileService.downloadFile(idGenre,fileName,request,"genre");
    }

    //Download the respective movie/serie image
    @GetMapping("/api/files/movieOrSerieImage/{idMovieOrSerie}/{fileName}")
    public ResponseEntity downloadSerieOrMovieImage(@PathVariable(value = "idMovieOrSerie")Long idMovieOrSerie, @PathVariable(value = "fileName")String fileName,HttpServletRequest request){
        return fileService.downloadFile(idMovieOrSerie,fileName,request,"movieOrSerie");
    }

}
