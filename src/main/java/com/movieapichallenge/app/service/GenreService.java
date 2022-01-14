package com.movieapichallenge.app.service;


import com.movieapichallenge.app.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface GenreService {

    public Iterable<Genre> findAll();

    public Page<Genre> findAll(Pageable pageable);

    public Optional<Genre> findById(Long id);

    public Genre save(Genre genre);

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> readById(Long genreId);

    ResponseEntity<?> readAll();

    ResponseEntity<?> saveNewGenre(String genre, MultipartFile multipartFile);

    ResponseEntity<?> updateGenre(String jsonData,MultipartFile multipartFile,Long genreId);
}
