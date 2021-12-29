package com.movieapichallenge.app.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    ResponseEntity downloadFileByIdCharacter(Long idCharacter, String fileName, HttpServletRequest request);
}
