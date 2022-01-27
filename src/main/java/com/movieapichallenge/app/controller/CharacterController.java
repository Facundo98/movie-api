package com.movieapichallenge.app.controller;

import com.movieapichallenge.app.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    CharacterService characterService;

    //Create a new character
    @PostMapping()
    public ResponseEntity<?> create(@RequestParam("character") String character, @RequestParam(value = "file")MultipartFile multipartFile){
        return characterService.saveNewCharacter(character,multipartFile);
    }

    //Read all characters
    @GetMapping()
    public ResponseEntity<?> readAll(){
         return characterService.readAll();
    }

    //Read a character by name
    @GetMapping(params = "name")
    public ResponseEntity<?> readByName(@RequestParam(value = "name")String name){
        return characterService.readByName(name);
    }

    //Read a character by name and age
    @GetMapping(params = {"name","age"})
    public ResponseEntity<?> readByNameAndAge(@RequestParam(value = "name")String name,@RequestParam(value = "age")Integer age){
        return characterService.readByNameAndAge(name,age);
    }

    //Read a character by name and weight
    @GetMapping(params = {"name","weight"})
    public ResponseEntity<?> readByNameAndWeight(@RequestParam(value = "name")String name,@RequestParam(value = "weight")Float weight){
        return characterService.readByNameAndWeight(name,weight);
    }

    //Read a character by name and movie/serie id
    @GetMapping(params = {"name","movies"})
    public ResponseEntity<?> readByNameAndMovieOrSerie(@RequestParam(value = "name")String name,@RequestParam(value = "movies")Long movies){
        return characterService.readByNameAndMovieOrSerieId(name,movies);
    }

    //Update a character
    @PutMapping("/{idCharacter}")
    public ResponseEntity<?> update(@RequestParam("character") String character, @RequestParam(value = "file")MultipartFile multipartFile,@PathVariable(value = "idCharacter") Long idCharacter){
        return characterService.updateCharacter(character,multipartFile,idCharacter);

    }

    //Delete a character
    @DeleteMapping("/{idCharacter}")
    public ResponseEntity<?> delete(@PathVariable Long idCharacter){
        return characterService.deleteById(idCharacter);
    }


}
