package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    CharacterService characterService;

    //Create a new character
    @PostMapping()
    public ResponseEntity<?> create(@RequestParam("character") String character, @RequestParam(value = "file")MultipartFile multipartFile){
        return characterService.saveNewCharacter(character,multipartFile);
    }

    //Read a character
    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(value = "id")Long characterId){
         return characterService.readById(characterId);
    }

    //Read all characters
    @GetMapping
    public ResponseEntity<?> readAll(){
        List<Character> characters = StreamSupport
                .stream(characterService.findAll().spliterator(),false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(characters);
    }

    //Update a character
    @PutMapping("/{idCharacter}")
    public ResponseEntity<?> update(@RequestParam("character") String character, @RequestParam(value = "file" , required = false)MultipartFile multipartFile,@PathVariable(value = "idCharacter") Long idCharacter){
        return characterService.updateCharacter(character,multipartFile,idCharacter);

    }

    //Delete a character
    @DeleteMapping("/{idCharacter}")
    public ResponseEntity<?> delete(@PathVariable Long idCharacter){
        return characterService.deleteById(idCharacter);
    }
}
