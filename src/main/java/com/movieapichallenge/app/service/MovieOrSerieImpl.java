package com.movieapichallenge.app.service;

import com.movieapichallenge.app.entity.MovieOrSerie;
import com.movieapichallenge.app.repository.MovieOrSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieOrSerieImpl implements MovieOrSerieService{

    @Autowired
    MovieOrSerieRepository movieOrSerieRepository;

    @Override
    public Iterable<MovieOrSerie> findAll() {
        return movieOrSerieRepository.findAll();
    }

    @Override
    public Page<MovieOrSerie> findAll(Pageable pageable) {
        return movieOrSerieRepository.findAll(pageable);
    }

    @Override
    public Optional<MovieOrSerie> findById(Long id) {
        return movieOrSerieRepository.findById(id);
    }

    @Override
    public MovieOrSerie save(MovieOrSerie movieOrSerie) {
        return movieOrSerieRepository.save(movieOrSerie);
    }

    @Override
    public void deleteById(Long id) {
        movieOrSerieRepository.deleteById(id);
    }
}
