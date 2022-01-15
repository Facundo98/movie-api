package com.movieapichallenge.app.service;

import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MovieOrSerieService {

    Iterable<MovieOrSerie> findAll();

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> readById(Long movieOrSerieId);

    ResponseEntity<?> readAll();

    ResponseEntity<?> create(String movieOrSerie, MultipartFile multipartFile);

    ResponseEntity<?> update(String jsonData,MultipartFile multipartFile,Long movieOrSerieId);
}
