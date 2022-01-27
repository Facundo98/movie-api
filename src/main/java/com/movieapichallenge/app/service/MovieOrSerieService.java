package com.movieapichallenge.app.service;

import com.movieapichallenge.app.dto.MovieOrSeriesInfo;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

public interface MovieOrSerieService {

    Iterable<MovieOrSerie> findAll();

    ResponseEntity<?> deleteById(Long id);

    Optional<MovieOrSerie> findById(Long movieOrSerieId);

    ResponseEntity<?> readAll();

    ResponseEntity<?> create(String movieOrSerie, MultipartFile multipartFile);

    ResponseEntity<?> update(String jsonData,MultipartFile multipartFile,Long movieOrSerieId);

    ResponseEntity<?> enrollCharacter(Long movieOrSerieId, Long characterId);

    ResponseEntity<?> deleteCharacter(Long movieOrSerieId, Long characterId);

    ResponseEntity<?> readByTittle(String tittle, String order);

    MovieOrSeriesInfo setMovieOrSerieInfo(Set<Character> charactersEnrolled, MovieOrSerie movieOrSerie);

    ResponseEntity<?> readByTittleAndGenre(String tittle, Long genreId, String order);
}
