package com.movieapichallenge.app.service;

import com.movieapichallenge.app.payloads.request.LoginRequest;
import com.movieapichallenge.app.payloads.request.SingUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AuthService {

    ResponseEntity<?> registerUser(@Valid @RequestBody SingUpRequest singUpRequest);

    ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);
}
