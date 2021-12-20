package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    //Download the respective character image
    @GetMapping("/api/files/characterImage/{idCharacter}/{fileName}")
    public ResponseEntity downloadCharacterImage(@PathVariable( value = "idCharacter") String idCharacter, @PathVariable(value = "fileName") String fileName){

        return fileService.downloadFileByIdCharacter(idCharacter,fileName);
    }

}
