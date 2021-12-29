package com.movieapichallenge.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.repository.CharacterRepository;
import com.movieapichallenge.app.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<?> deleteById(Long id) {
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        FileUtil fileUtil = new FileUtil();

        if(!optionalCharacter.isPresent()){
            return ResponseEntity.notFound().build();
        }

        try {
            FileUtils.deleteDirectory(new File(fileUtil.getCharacterImageStoragePath() + optionalCharacter.get().getId()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        characterRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readById(Long characterId) {
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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        Set<ConstraintViolation<Character>> violations;

        ObjectMapper mapper = new ObjectMapper();
        try {
            character = mapper.readValue(c,Character.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        validator = factory.getValidator();
        violations = validator.validate(character);
        if(!violations.isEmpty() || multipartFile.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        if(multipartFile != null) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            character.setImage(fileName);
            charac = characterRepository.save(character);
            uploadDir = fileUtil.getCharacterImageStoragePath() + charac.getId();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            try {
                InputStream inputStream = multipartFile.getInputStream();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            }
            return ResponseEntity.status(HttpStatus.CREATED).body(charac);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(characterRepository.save(character));

    }

    @Override
    public ResponseEntity<?> updateCharacter(String jsonData, MultipartFile multipartFile, Long characterId) {
        Optional<Character> characterOptional = characterRepository.findById(characterId);
        ObjectMapper mapper;
        Character character;
        FileUtil fileUtil = new FileUtil();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        Set<ConstraintViolation<Character>> violations;

        if(!characterOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }

        mapper = new ObjectMapper();
        try {
            character = mapper.readValue(jsonData,Character.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        validator = factory.getValidator();
        violations = validator.validate(character);
        if(!violations.isEmpty() || multipartFile == null){
            return ResponseEntity.badRequest().build();
        }

        try {
            Files.delete(fileUtil.getImagePath(characterOptional.get().getId(), characterOptional.get().getImage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = fileUtil.getImagePath(characterOptional.get().getId(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        characterOptional.get().setImage(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        characterOptional.get().setName(character.getName());
        characterOptional.get().setAge(character.getAge());
        characterOptional.get().setWeight(character.getWeight());
        characterOptional.get().setHistory(character.getHistory());
        characterOptional.get().setMovieOrSerieList(character.getMovieOrSerieList());

        return ResponseEntity.status(HttpStatus.OK).body(characterRepository.save(characterOptional.get()));
    }
}
