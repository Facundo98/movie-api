package com.movieapichallenge.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movieapichallenge.app.entity.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CharacterService {

    Iterable<Character> findAll();

    Page<Character> findAll(Pageable pageable);

    Optional<Character> findById(Long id);

    Character save(Character character);

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> readById(Long characterId);

    ResponseEntity<?> readAll();

    ResponseEntity<?> saveNewCharacter(String character, MultipartFile multipartFile);

    ResponseEntity<?> updateCharacter(String jsonData,MultipartFile multipartFile,Long characterId);
}
