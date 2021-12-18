package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    CharacterService characterService;

    //Create a new character
    @PostMapping
    public ResponseEntity<Character> create(@Valid @RequestBody Character character){
        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.save(character));
    }

    //Read a character
    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id")Long characterId){
         return characterService.readById(characterId);
    }
}
