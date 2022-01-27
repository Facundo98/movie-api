package com.movieapichallenge.app.controller;


import com.movieapichallenge.app.dto.request.LoginRequest;
import com.movieapichallenge.app.dto.request.SingUpRequest;
import com.movieapichallenge.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SingUpRequest singUpRequest) {
        return authService.registerUser(singUpRequest);
    }


}
