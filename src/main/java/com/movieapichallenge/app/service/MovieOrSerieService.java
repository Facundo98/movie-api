package com.movieapichallenge.app.service;

import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MovieOrSerieService {

    public Iterable<MovieOrSerie> findAll();

    public Page<MovieOrSerie> findAll(Pageable pageable);

    public Optional<MovieOrSerie> findById(Long id);

    public MovieOrSerie save(MovieOrSerie movieOrSerie);

    public void deleteById(Long id);
}
