package com.movieapichallenge.app.repository;

import com.movieapichallenge.app.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository <Character,Long> {
}
