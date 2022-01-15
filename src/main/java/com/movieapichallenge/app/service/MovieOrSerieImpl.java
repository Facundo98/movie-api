package com.movieapichallenge.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.entity.MovieOrSerie;
import com.movieapichallenge.app.repository.MovieOrSerieRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieOrSerieImpl implements MovieOrSerieService{

    @Autowired
    MovieOrSerieRepository movieOrSerieRepository;

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
        uploadDir = fileUtil.getMovieOrSerieImageStoragePath() + movieOrSerie1.getId();
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
    @Transactional(readOnly = true)
    public ResponseEntity<?> readById(Long movieOrSerieId) {
        Optional<MovieOrSerie> optionalMovieOrSerie = movieOrSerieRepository.findById(movieOrSerieId);
        String fileBasePath = "http://localhost:8080/api/files/movieOrSerieImage/";

        if(!optionalMovieOrSerie.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        optionalMovieOrSerie.get().setImage( fileBasePath + optionalMovieOrSerie.get().getId() + "/" + optionalMovieOrSerie.get().getImage());

        return ResponseEntity.status(HttpStatus.OK).body(optionalMovieOrSerie.get());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readAll() {
        List<MovieOrSerie> movieOrSerieList = StreamSupport
                .stream(this.findAll().spliterator(),false)
                .collect(Collectors.toList());

        if(movieOrSerieList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(movieOrSerieList);
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
            Files.delete(fileUtil.getMovieOrSeriePath(movieOrSerieOptional.get().getId(), movieOrSerieOptional.get().getImage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = fileUtil.getMovieOrSeriePath(movieOrSerieOptional.get().getId(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        movieOrSerieOptional.get().setImage(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        movieOrSerieOptional.get().setTittle(movieOrSerie.getTittle());
        movieOrSerieOptional.get().setCreationDate(movieOrSerie.getCreationDate());
        movieOrSerieOptional.get().setScore(movieOrSerie.getScore());
        movieOrSerieOptional.get().setCharacters(movieOrSerie.getCharacters());
        movieOrSerieOptional.get().setGenres(movieOrSerie.getGenres());

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
            FileUtils.deleteDirectory(new File(fileUtil.getMovieOrSerieImageStoragePath() + optionalMovieOrSerie.get().getId()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        movieOrSerieRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
