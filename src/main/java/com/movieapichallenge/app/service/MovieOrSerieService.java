package com.movieapichallenge.app.service;

import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface MovieOrSerieService {

    public Iterable<MovieOrSerie> findAll();

    public Page<MovieOrSerie> findAll(Pageable pageable);

    public Optional<MovieOrSerie> findById(Long id);

    public MovieOrSerie save(MovieOrSerie movieOrSerie);

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> readById(Long movieOrSerieId);

    ResponseEntity<?> readAll();

    ResponseEntity<?> saveNewMovieOrSerie(String movieOrSerie, MultipartFile multipartFile);

    ResponseEntity<?> updateMovieOrSerie(String jsonData,MultipartFile multipartFile,Long movieOrSerieId);
}
