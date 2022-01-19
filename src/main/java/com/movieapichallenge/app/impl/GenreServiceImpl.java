package com.movieapichallenge.app.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapichallenge.app.entity.Character;
import com.movieapichallenge.app.entity.Genre;
import com.movieapichallenge.app.repository.GenreRepository;
import com.movieapichallenge.app.service.GenreService;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Genre> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteById(Long id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        FileUtil fileUtil = new FileUtil();

        if(!optionalGenre.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            FileUtils.deleteDirectory(new File(fileUtil.getGenreImageStoragePath() + optionalGenre.get().getId()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        genreRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readById(Long genreId) {
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);
        String fileBasePath = "http://localhost:8080/api/files/genreImage/";

        if(!optionalGenre.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        optionalGenre.get().setImage( fileBasePath + optionalGenre.get().getId() + "/" + optionalGenre.get().getImage());

        return ResponseEntity.status(HttpStatus.OK).body(optionalGenre.get());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> readAll() {
        List<Genre> genres = StreamSupport
                .stream(this.findAll().spliterator(),false)
                .collect(Collectors.toList());

        if(genres.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(genres);
    }

    @Override
    public ResponseEntity<?> saveNewGenre(String g, MultipartFile multipartFile) {
        String fileName,uploadDir;
        Genre gen,genre;
        FileUtil fileUtil = new FileUtil();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        Set<ConstraintViolation<Genre>> violations;

        ObjectMapper mapper = new ObjectMapper();
        try {
            genre = mapper.readValue(g,Genre.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        validator = factory.getValidator();
        violations = validator.validate(genre);
        if(!violations.isEmpty() || multipartFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        genre.setImage(fileName);
        gen = genreRepository.save(genre);
        uploadDir = fileUtil.getGenreImageStoragePath() + gen.getId();
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

        return ResponseEntity.status(HttpStatus.CREATED).body(gen);

    }

    @Override
    public ResponseEntity<?> updateGenre(String jsonData, MultipartFile multipartFile, Long genreId) {
        Optional<Genre> genreOptional = genreRepository.findById(genreId);
        ObjectMapper mapper;
        Genre genre;
        FileUtil fileUtil = new FileUtil();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        Set<ConstraintViolation<Genre>> violations;

        if(!genreOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        mapper = new ObjectMapper();
        try {
            genre = mapper.readValue(jsonData,Genre.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        validator = factory.getValidator();
        violations = validator.validate(genre);
        if(!violations.isEmpty() || multipartFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Files.delete(fileUtil.getGenrePath(genreOptional.get().getId(), genreOptional.get().getImage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = fileUtil.getGenrePath(genreOptional.get().getId(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        genreOptional.get().setImage(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        genreOptional.get().setName(genre.getName());
        genreOptional.get().setMovieOrSerieList(genre.getMovieOrSerieList());

        return ResponseEntity.status(HttpStatus.OK).body(genreRepository.save(genreOptional.get()));
    }


}
