package com.movieapichallenge.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.repository.CharacterRepository;
import com.movieapichallenge.app.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Character> readById(Long characterId) {
        Optional<Character> optionalCharacter = characterRepository.findById(characterId);
        String fileBasePath = "http://localhost:8080/api/files/characterImage/";

        if(!optionalCharacter.isPresent()){
            return ResponseEntity.notFound().build();
        }
        optionalCharacter.get().setImage( fileBasePath + optionalCharacter.get().getId() + "/" + optionalCharacter.get().getImage());

        return ResponseEntity.status(HttpStatus.OK).body(optionalCharacter.get());
    }

    @Override
    public ResponseEntity<?> saveNewCharacter(String c, MultipartFile multipartFile) {
        String fileName,uploadDir;
        Character charac,character;
        FileUtil fileUtil = new FileUtil();

        ObjectMapper mapper = new ObjectMapper();
        try {
            character = mapper.readValue(c,Character.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        character.setImage(fileName);
        charac =  characterRepository.save(character);
        uploadDir = fileUtil.getCharacterImagePath() + charac.getId();
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(charac);
    }
}
