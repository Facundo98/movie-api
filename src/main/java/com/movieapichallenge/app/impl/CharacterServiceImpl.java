package com.movieapichallenge.app.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.dto.CharacterDTO;
import com.movieapichallenge.app.dto.CharacterInfo;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.entity.MovieOrSerie;
import com.movieapichallenge.app.dto.response.MessageResponse;
import com.movieapichallenge.app.repository.CharacterRepository;
import com.movieapichallenge.app.service.CharacterService;
import com.movieapichallenge.app.service.MovieOrSerieService;
import com.movieapichallenge.app.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    MovieOrSerieService movieOrSerieService;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Character> findAll() {
        return characterRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Character> findById(Long id) {
        return characterRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteById(Long id) {
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        FileUtil fileUtil = new FileUtil();

        if(optionalCharacter.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            FileUtils.deleteDirectory(new File(fileUtil.getCharacterImageStoragePath() + optionalCharacter.get().getCharacterId()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        characterRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readAll() {
        FileUtil fileUtil = new FileUtil();

        List<Character> characterList = StreamSupport
                .stream(this.findAll().spliterator(),false)
                .collect(Collectors.toList());
        Set<CharacterDTO> characterDTOSet = null;
        CharacterDTO characterDTO;

        if(characterList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        characterDTOSet = new HashSet<>();
        for(Character character : characterList){
            characterDTO = new CharacterDTO();
            characterDTO.setName(character.getName());
            characterDTO.setImage(fileUtil.getCharacterFileBasePath() + character.getCharacterId() + "/" + character.getImage());
            characterDTOSet.add(characterDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(characterDTOSet);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        validator = factory.getValidator();
        violations = validator.validate(character);
        if(!violations.isEmpty() || multipartFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        character.setImage(fileName);
        charac = characterRepository.save(character);
        uploadDir = fileUtil.getCharacterImageStoragePath() + charac.getCharacterId();
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
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Character saved successfully!"));


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

        if(characterOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        mapper = new ObjectMapper();
        try {
            character = mapper.readValue(jsonData,Character.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        validator = factory.getValidator();
        violations = validator.validate(character);
        if(!violations.isEmpty() || multipartFile == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Files.delete(fileUtil.getImagePath(characterOptional.get().getCharacterId(), characterOptional.get().getImage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = fileUtil.getImagePath(characterOptional.get().getCharacterId(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        characterOptional.get().setImage(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        characterOptional.get().setName(character.getName());
        characterOptional.get().setAge(character.getAge());
        characterOptional.get().setWeight(character.getWeight());
        characterOptional.get().setHistory(character.getHistory());
        characterRepository.save(characterOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Character update successfully"));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readByName(String name) {
        Optional<Character> optionalCharacter = characterRepository.findByName(name);

        if(optionalCharacter.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: there is no character with the name provided"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(setCharacterInfo(optionalCharacter.get().getMovieOrSeriesEnrolled(),optionalCharacter.get()));

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readByNameAndAge(String name, Integer age) {
        Optional<Character> optionalCharacter = characterRepository.findByNameAndAge(name,age);

        if(optionalCharacter.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: there is no character with the name and age provided"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(setCharacterInfo(optionalCharacter.get().getMovieOrSeriesEnrolled(),optionalCharacter.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readByNameAndWeight(String name, Float weight) {
        Optional<Character> optionalCharacter = characterRepository.findByNameAndWeight(name,weight);

        if(optionalCharacter.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: there is no character with the name and weight provided"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(setCharacterInfo(optionalCharacter.get().getMovieOrSeriesEnrolled(),optionalCharacter.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readByNameAndMovieOrSerieId(String name, Long movieOrSerieId) {
        Optional<Character> optionalCharacter = characterRepository.findByMovieOrSeriesEnrolled_Id(movieOrSerieId,name);

        if(optionalCharacter.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: there is no character with the name and movie/serie id provided"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(setCharacterInfo(optionalCharacter.get().getMovieOrSeriesEnrolled(),optionalCharacter.get()));
    }

    public CharacterInfo setCharacterInfo(Set<MovieOrSerie> movieOrSeriesEnrolled,Character character){
        FileUtil fileUtil = new FileUtil();
        CharacterInfo characterInfo;

        characterInfo = new CharacterInfo();
        characterInfo.setName(character.getName());
        characterInfo.setImage(fileUtil.getCharacterFileBasePath() + character.getCharacterId() + "/" + character.getImage());
        characterInfo.setAge(character.getAge());
        characterInfo.setHistory(character.getHistory());
        characterInfo.setWeight(character.getWeight());
        Set<Long> movieOrSeriesIds = new HashSet<>();
        for(MovieOrSerie movieOrSerie : movieOrSeriesEnrolled){
            movieOrSeriesIds.add(movieOrSerie.getMovieOrSerieId());
        }
        characterInfo.setMovieOrSeriesIds(movieOrSeriesIds);

        return characterInfo;
    }
}
