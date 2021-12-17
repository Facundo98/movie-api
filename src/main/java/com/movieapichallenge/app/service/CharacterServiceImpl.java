package com.movieapichallenge.app.service;

import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService{

    @Autowired
    CharacterRepository characterRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Character> findAll() {
        return characterRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Character> findAll(Pageable pageable) {
        return characterRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Character> findById(Long id) {
        return characterRepository.findById(id);
    }

    @Override
    @Transactional
    public Character save(Character character) {
        return characterRepository.save(character);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        characterRepository.deleteById(id);
    }
}
