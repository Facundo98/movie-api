package com.movieapichallenge.app.repository;

import com.movieapichallenge.app.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository <Genre,Long> {
}
