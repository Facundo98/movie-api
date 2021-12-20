package com.movieapichallenge.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movieapichallenge.app.entity.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CharacterService {

    public Iterable<Character> findAll();

    public Page<Character> findAll(Pageable pageable);

    public Optional<Character> findById(Long id);

    public Character save(Character character);

    public void deleteById(Long id);

    ResponseEntity<?> readById(Long characterId);

    ResponseEntity<?> saveNewCharacter(String character, MultipartFile multipartFile);
}
