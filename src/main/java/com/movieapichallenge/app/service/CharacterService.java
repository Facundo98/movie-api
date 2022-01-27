package com.movieapichallenge.app.service;

import com.movieapichallenge.app.dto.CharacterInfo;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

public interface CharacterService {

    Iterable<Character> findAll();

    Optional<Character> findById(Long id);

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> readAll();

    ResponseEntity<?> saveNewCharacter(String character, MultipartFile multipartFile);

    ResponseEntity<?> updateCharacter(String jsonData,MultipartFile multipartFile,Long characterId);

    ResponseEntity<?> readByName(String name);

    ResponseEntity<?> readByNameAndAge(String name, Integer age);

    ResponseEntity<?> readByNameAndWeight(String name, Float weight);

    ResponseEntity<?> readByNameAndMovieOrSerieId(String name, Long movieOrSerieId);

    CharacterInfo setCharacterInfo(Set<MovieOrSerie> movieOrSeriesEnrolled, Character character);
}
