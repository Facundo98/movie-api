package com.movieapichallenge.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.entity.Genre;
import com.movieapichallenge.app.entity.MovieOrSerie;
import com.movieapichallenge.app.repository.MovieOrSerieRepository;
import com.movieapichallenge.app.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieOrSerieImpl implements MovieOrSerieService{

    @Autowired
    MovieOrSerieRepository movieOrSerieRepository;

    @Override
    public Iterable<MovieOrSerie> findAll() {
        return movieOrSerieRepository.findAll();
    }

    @Override
    public Page<MovieOrSerie> findAll(Pageable pageable) {
        return movieOrSerieRepository.findAll(pageable);
    }

    @Override
    public Optional<MovieOrSerie> findById(Long id) {
        return movieOrSerieRepository.findById(id);
    }

    @Override
    public MovieOrSerie save(MovieOrSerie movieOrSerie) {
        return movieOrSerieRepository.save(movieOrSerie);
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        return null;
    }


    @Override
    public ResponseEntity<?> readById(Long movieOrSerieId) {
        return null;
    }

    @Override
    public ResponseEntity<?> readAll() {
        return null;
    }

    @Override
    public ResponseEntity<?> saveNewMovieOrSerie(String movieOrSerie, MultipartFile multipartFile) {
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
    public ResponseEntity<?> updateMovieOrSerie(String jsonData, MultipartFile multipartFile, Long movieOrSerieId) {
        return null;
    }
}
