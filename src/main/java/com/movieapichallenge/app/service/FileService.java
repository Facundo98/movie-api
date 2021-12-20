package com.movieapichallenge.app.service;

import org.springframework.http.ResponseEntity;

public interface FileService {

    ResponseEntity downloadFileByIdCharacter( String idCharacter, String fileName);
}
