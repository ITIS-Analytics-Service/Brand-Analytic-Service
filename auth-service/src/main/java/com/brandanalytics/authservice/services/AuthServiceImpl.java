package com.brandanalytics.authservice.services;

import com.brandanalytics.authservice.dto.AuthResponse;
import com.brandanalytics.authservice.dto.RegisterResponse;
import com.brandanalytics.authservice.dto.UserCreateRequest;
import com.brandanalytics.authservice.dto.UserLoginRequest;
import com.brandanalytics.authservice.exceptions.UserEmailAlreadyExistsException;
import com.brandanalytics.authservice.models.Role;
import com.brandanalytics.authservice.models.User;
import com.brandanalytics.authservice.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public RegisterResponse createUser(UserCreateRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException();
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getId(), savedUser.getEmail(), Instant.now());
    }

    @Override
    public AuthResponse authenticate(UserLoginRequest request) {
        return null;
    }


    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
