package com.movieapichallenge.app.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.dto.MovieOrSerieDTO;
import com.movieapichallenge.app.dto.MovieOrSeriesInfo;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.entity.MovieOrSerie;
import com.movieapichallenge.app.dto.response.MessageResponse;
import com.movieapichallenge.app.repository.MovieOrSerieRepository;
import com.movieapichallenge.app.service.CharacterService;
import com.movieapichallenge.app.service.MovieOrSerieService;
import com.movieapichallenge.app.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieOrSerieImpl implements MovieOrSerieService {

    @Autowired
    MovieOrSerieRepository movieOrSerieRepository;

    @Lazy
    @Autowired
    CharacterService characterService;

    @Override
    @Transactional
    public ResponseEntity<?> create(String movieOrSerie, MultipartFile multipartFile) {
        String fileName,uploadDir;
        MovieOrSerie mov,movieOrSerie1;
        FileUtil fileUtil = new FileUtil();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        Set<ConstraintViolation<MovieOrSerie>> violations;

        ObjectMapper mapper = new ObjectMapper();
        try {
            mov = mapper.readValue(movieOrSerie,MovieOrSerie.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        validator = factory.getValidator();
        violations = validator.validate(mov);
        if(!violations.isEmpty() || multipartFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        mov.setImage(fileName);
        movieOrSerie1 = movieOrSerieRepository.save(mov);
        uploadDir = fileUtil.getMovieOrSerieImageStoragePath() + movieOrSerie1.getMovieOrSerieId();
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

        return ResponseEntity.status(HttpStatus.CREATED).body(movieOrSerie1);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieOrSerie> findAll() {
        return movieOrSerieRepository.findAll();
    }


    @Override
    public Optional<MovieOrSerie> findById(Long movieOrSerieId) {
        return movieOrSerieRepository.findById(movieOrSerieId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readAll() {
        List<MovieOrSerie> movieOrSerieList = StreamSupport
                .stream(this.findAll().spliterator(),false)
                .collect(Collectors.toList());
        Set<MovieOrSerieDTO> movieOrSerieDTOSet = null;
        MovieOrSerieDTO movieOrSerieDTO;
        FileUtil fileUtil = new FileUtil();

        if(movieOrSerieList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        movieOrSerieDTOSet = new HashSet<>();
        for(MovieOrSerie movieOrSerie : movieOrSerieList){
            movieOrSerieDTO = new MovieOrSerieDTO();
            movieOrSerieDTO.setTittle(movieOrSerie.getTittle());
            movieOrSerieDTO.setImage(fileUtil.getMovieOrSerieFileBasePath() + movieOrSerie.getMovieOrSerieId() + "/" + movieOrSerie.getImage());
            movieOrSerieDTO.setCreationDate(movieOrSerie.getCreationDate());
            movieOrSerieDTOSet.add(movieOrSerieDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(movieOrSerieDTOSet);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readByTittle(String tittle, String order) {
        Optional<Set<MovieOrSerie>> optionalMovieOrSerie = null;
        Set<MovieOrSeriesInfo> movieOrSeriesInfoSet;

        if(order == null){
            optionalMovieOrSerie = movieOrSerieRepository.findByTittleContains(tittle);
        }else {
            if (!order.equals("ASC") && !order.equals("DESC")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: the order parameter must be ASC or DESC only"));
            }

            if (order.equals("ASC")) {
                optionalMovieOrSerie = movieOrSerieRepository.findByTittleContainsOrderByCreationDateAsc(tittle);
            }

            if (order.equals("DESC")) {
                optionalMovieOrSerie = movieOrSerieRepository.findByTittleContainsOrderByCreationDateDesc(tittle);
            }

            if (optionalMovieOrSerie.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: there is no movie/serie with the tittle provided"));
            }
        }
        movieOrSeriesInfoSet = new LinkedHashSet<>();
        for(MovieOrSerie movieOrSerie : optionalMovieOrSerie.get()){
            movieOrSeriesInfoSet.add(setMovieOrSerieInfo(movieOrSerie.getCharactersEnrolled(),movieOrSerie));
        }
        return ResponseEntity.status(HttpStatus.OK).body(movieOrSeriesInfoSet);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readByTittleAndGenre(String tittle, Long genreId, String order) {
        Optional<Set<MovieOrSerie>> optionalMovieOrSerie = null;
        Set<MovieOrSeriesInfo> movieOrSeriesInfoSet;


        if(order == null){
            optionalMovieOrSerie = movieOrSerieRepository.findByTittleAndGenreId(genreId,tittle);
        }else {
            if (!order.equals("ASC") && !order.equals("DESC")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: the order parameter must be ASC or DESC only"));
            }

            if (order.equals("ASC")) {
                optionalMovieOrSerie = movieOrSerieRepository.findByTittleAndGenreIdAsc(genreId, tittle);
            }

            if (order.equals("DESC")) {
                optionalMovieOrSerie = movieOrSerieRepository.findByTittleAndGenreIdDesc(genreId, tittle);
            }

            if (optionalMovieOrSerie.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: there is no movie/serie with the tittle and genre provided"));
            }
        }
        movieOrSeriesInfoSet = new LinkedHashSet<>();
        for(MovieOrSerie movieOrSerie : optionalMovieOrSerie.get()){
            movieOrSeriesInfoSet.add(setMovieOrSerieInfo(movieOrSerie.getCharactersEnrolled(),movieOrSerie));
        }
        return ResponseEntity.status(HttpStatus.OK).body(movieOrSeriesInfoSet);
    }

    @Override
    @Transactional
    public ResponseEntity<?> update(String jsonData, MultipartFile multipartFile, Long movieOrSerieId) {
        Optional<MovieOrSerie> movieOrSerieOptional = movieOrSerieRepository.findById(movieOrSerieId);
        ObjectMapper mapper;
        MovieOrSerie movieOrSerie;
        FileUtil fileUtil = new FileUtil();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        Set<ConstraintViolation<MovieOrSerie>> violations;

        if(!movieOrSerieOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        mapper = new ObjectMapper();
        try {
            movieOrSerie = mapper.readValue(jsonData,MovieOrSerie.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        validator = factory.getValidator();
        violations = validator.validate(movieOrSerie);
        if(!violations.isEmpty() || multipartFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Files.delete(fileUtil.getMovieOrSeriePath(movieOrSerieOptional.get().getMovieOrSerieId(), movieOrSerieOptional.get().getImage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = fileUtil.getMovieOrSeriePath(movieOrSerieOptional.get().getMovieOrSerieId(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        movieOrSerieOptional.get().setImage(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        movieOrSerieOptional.get().setTittle(movieOrSerie.getTittle());
        movieOrSerieOptional.get().setCreationDate(movieOrSerie.getCreationDate());
        movieOrSerieOptional.get().setScore(movieOrSerie.getScore());

        return ResponseEntity.status(HttpStatus.OK).body(movieOrSerieRepository.save(movieOrSerieOptional.get()));
    }

    @Override
    public ResponseEntity<?> enrollCharacter(Long movieOrSerieId, Long characterId) {
        Optional<MovieOrSerie> movieOrSerieOptional = movieOrSerieRepository.findById(movieOrSerieId);
        Optional<Character> optionalCharacter = characterService.findById(characterId);
        Set characterHashSet;

        if(!movieOrSerieOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: movie/serie id does not exists!"));
        }

        if(!optionalCharacter.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: character id does not exists!"));
        }

        characterHashSet = movieOrSerieOptional.get().getCharactersEnrolled();
        characterHashSet.add(optionalCharacter.get());

        movieOrSerieOptional.get().setCharactersEnrolled(characterHashSet);

        return ResponseEntity.status(HttpStatus.OK).body(movieOrSerieRepository.save(movieOrSerieOptional.get()));
    }

    @Override
    public ResponseEntity<?> deleteCharacter(Long movieOrSerieId, Long characterId) {
        Optional<MovieOrSerie> movieOrSerieOptional = movieOrSerieRepository.findById(movieOrSerieId);
        Optional<Character> optionalCharacter = characterService.findById(characterId);
        Set characterHashSet;

        if(!movieOrSerieOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: movie/serie id does not exists!"));
        }

        if(!optionalCharacter.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: character id does not exists!"));
        }

        if(!movieOrSerieOptional.get().getCharactersEnrolled().contains(optionalCharacter.get())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: the character is not enrolled with the movie/serie"));
        }

        characterHashSet = movieOrSerieOptional.get().getCharactersEnrolled();
        characterHashSet.remove(optionalCharacter.get());
        movieOrSerieOptional.get().setCharactersEnrolled(characterHashSet);

        return ResponseEntity.status(HttpStatus.OK).body(movieOrSerieRepository.save(movieOrSerieOptional.get()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteById(Long id) {
        Optional<MovieOrSerie> optionalMovieOrSerie = movieOrSerieRepository.findById(id);
        FileUtil fileUtil = new FileUtil();

        if(!optionalMovieOrSerie.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            FileUtils.deleteDirectory(new File(fileUtil.getMovieOrSerieImageStoragePath() + optionalMovieOrSerie.get().getMovieOrSerieId()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        movieOrSerieRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public MovieOrSeriesInfo setMovieOrSerieInfo(Set<Character> charactersEnrolled, MovieOrSerie movieOrSerie){
        FileUtil fileUtil = new FileUtil();
        MovieOrSeriesInfo movieOrSeriesInfo;
        Set<Long> charactersIds = new HashSet<>();

        for(Character character : charactersEnrolled){
            charactersIds.add(character.getCharacterId());
        }
        movieOrSeriesInfo = new MovieOrSeriesInfo(
                movieOrSerie.getTittle(),
                fileUtil.getMovieOrSerieFileBasePath() + movieOrSerie.getMovieOrSerieId() + "/" + movieOrSerie.getImage(),
                movieOrSerie.getCreationDate(),
                movieOrSerie.getScore(),
                charactersIds
        );

        return movieOrSeriesInfo;
    }
}
