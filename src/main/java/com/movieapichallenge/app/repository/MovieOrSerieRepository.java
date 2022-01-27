package com.movieapichallenge.app.repository;

import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.entity.MovieOrSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieOrSerieRepository extends JpaRepository <MovieOrSerie,Long> {

    Optional<Set<MovieOrSerie>> findByTittleContains(String tittle);

    Optional<Set<MovieOrSerie>> findByTittleContainsOrderByCreationDateAsc(String tittle);

    Optional<Set<MovieOrSerie>> findByTittleContainsOrderByCreationDateDesc(String tittle);

    @Query(value = "SELECT * FROM movie_or_serie m INNER JOIN movie_or_serie_genre g ON m.movie_or_serie_id=g.movie_or_serie_id AND m.tittle LIKE %:tittle% AND g.genre_id = :id",
            nativeQuery = true)
    Optional<Set<MovieOrSerie>> findByTittleAndGenreId(@Param("id")Long id, @Param("tittle")String tittle);

    @Query(value = "SELECT * FROM movie_or_serie m INNER JOIN movie_or_serie_genre g ON m.movie_or_serie_id=g.movie_or_serie_id AND " +
            "m.tittle LIKE %:tittle% AND g.genre_id = :id ORDER BY m.creation_date ASC",
            nativeQuery = true)
    Optional<Set<MovieOrSerie>> findByTittleAndGenreIdAsc(@Param("id")Long id, @Param("tittle")String tittle);

    @Query(value = "SELECT * FROM movie_or_serie m INNER JOIN movie_or_serie_genre g ON m.movie_or_serie_id=g.movie_or_serie_id " +
            "AND m.tittle LIKE %:tittle% AND g.genre_id = :id ORDER BY m.creation_date DESC",
            nativeQuery = true)
    Optional<Set<MovieOrSerie>> findByTittleAndGenreIdDesc(@Param("id")Long id, @Param("tittle")String tittle);
}
