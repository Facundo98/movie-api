package com.movieapichallenge.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    CharacterService characterService;

    //Create a new character
    @PostMapping()
    public ResponseEntity<?> create(@RequestParam("character") String character, @RequestParam("file")MultipartFile multipartFile){
        return characterService.saveNewCharacter(character,multipartFile);
    }

    //Read a character
    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id")Long characterId){
         return characterService.readById(characterId);
    }
}
