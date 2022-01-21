package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    GenreService genreService;

    //Create a new genre
    @PostMapping
    public ResponseEntity<?> create (@RequestParam(value = "genre") String genre, @RequestParam( value = "file")MultipartFile multipartFile){
        return genreService.saveNewGenre(genre,multipartFile);
    }

    //Read a genre
    @GetMapping("/{id}")
    public ResponseEntity<?> read (@PathVariable(value = "id")Long genreId){
        return genreService.readById(genreId);
    }

    //Read all genres
    @GetMapping
    public ResponseEntity<?> readAll(){
        return genreService.readAll();
    }

    //Update a genre
    @PutMapping("/{idGenre}")
    public ResponseEntity<?> update (@RequestParam(value = "genre")String genre, @RequestParam(value = "file")MultipartFile multipartFile, @PathVariable(value = "idGenre")Long idGenre){
        return genreService.updateGenre(genre,multipartFile,idGenre);
    }

    //Delete a genre
    @DeleteMapping("/{idGenre}")
    public ResponseEntity<?> delete(@PathVariable Long idGenre){
        return genreService.deleteById(idGenre);
    }


}
