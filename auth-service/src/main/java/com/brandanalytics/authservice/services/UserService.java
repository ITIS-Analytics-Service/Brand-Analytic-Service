package com.brandanalytics.authservice.services;

import com.brandanalytics.authservice.dto.AuthResponse;
import com.brandanalytics.authservice.dto.RegisterResponse;
import com.brandanalytics.authservice.dto.UserCreateRequest;
import com.brandanalytics.authservice.dto.UserLoginRequest;

public interface UserService {

    RegisterResponse createUser(UserCreateRequest request);

    AuthResponse authenticate(UserLoginRequest request);


}
