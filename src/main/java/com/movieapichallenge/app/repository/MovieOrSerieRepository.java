package com.movieapichallenge.app.repository;

import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieOrSerieRepository extends JpaRepository <MovieOrSerie,Long> {
}
