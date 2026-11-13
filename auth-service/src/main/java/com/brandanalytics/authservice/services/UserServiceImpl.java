package com.brandanalytics.authservice.services;

import com.brandanalytics.authservice.dto.AuthResponse;
import com.brandanalytics.authservice.dto.RegisterResponse;
import com.brandanalytics.authservice.dto.UserCreateRequest;
import com.brandanalytics.authservice.dto.UserLoginRequest;
import org.springframework.stereotype.Controller;

@Controller
public class UserServiceImpl implements UserService {
    @Override
    public RegisterResponse createUser(UserCreateRequest request) {
        return null;
    }

    @Override
    public AuthResponse authenticate(UserLoginRequest request) {
        return null;
    }
}
