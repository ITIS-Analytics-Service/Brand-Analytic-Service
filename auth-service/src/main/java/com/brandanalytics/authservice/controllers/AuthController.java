package com.brandanalytics.authservice.controllers;

import com.brandanalytics.authservice.dto.AuthResponse;
import com.brandanalytics.authservice.dto.RegisterResponse;
import com.brandanalytics.authservice.dto.UserCreateRequest;
import com.brandanalytics.authservice.dto.UserLoginRequest;
import com.brandanalytics.authservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
