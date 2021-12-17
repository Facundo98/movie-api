package com.movieapichallenge.app.service;


import com.movieapichallenge.app.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GenreService {

    public Iterable<Genre> findAll();

    public Page<Genre> findAll(Pageable pageable);

    public Optional<Genre> findById(Long id);

    public Genre save(Genre genre);

    public void deleteById(Long id);
}
