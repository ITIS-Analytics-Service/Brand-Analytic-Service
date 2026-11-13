package com.brandanalytics.authservice.controllers;

import com.brandanalytics.authservice.dto.AuthResponse;
import com.brandanalytics.authservice.dto.RegisterResponse;
import com.brandanalytics.authservice.dto.UserCreateRequest;
import com.brandanalytics.authservice.dto.UserLoginRequest;
import com.brandanalytics.authservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid UserCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid UserLoginRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
