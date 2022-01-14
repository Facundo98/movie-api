package com.movieapichallenge.app.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    ResponseEntity downloadFile(Long id, String fileName, HttpServletRequest request,String type);

}
