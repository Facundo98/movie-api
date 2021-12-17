package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    CharacterService characterService;

    //Create a new character
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Character character){
        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.save(character));
    }
}
