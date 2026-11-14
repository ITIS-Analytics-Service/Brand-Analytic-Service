package com.brandanalytics.authservice.services;

import com.brandanalytics.authservice.dto.*;
import jakarta.validation.Valid;

public interface AuthService {

    RegisterResponse createUser(UserCreateRequest request);

    TokenPair authenticate(UserLoginRequest request);


    TokenPair refreshToken(RefreshTokenRequest request);
}
