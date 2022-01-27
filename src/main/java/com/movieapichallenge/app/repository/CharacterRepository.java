package com.movieapichallenge.app.repository;

import com.movieapichallenge.app.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CharacterRepository extends JpaRepository <Character,Long> {

    Optional<Character> findByNameAndAge(String name,Integer age);

    Optional<Character> findByNameAndWeight(String name,Float weight);

    Optional<Character> findByName(String name);

    @Query(value = "SELECT * FROM characters c INNER JOIN movie_or_serie_characters m ON c.character_id=m.character_id AND c.name LIKE :name AND m.movie_or_serie_id = :id",
    nativeQuery = true)
    Optional<Character> findByMovieOrSeriesEnrolled_Id(@Param("id")Long id,@Param("name")String name);

}
