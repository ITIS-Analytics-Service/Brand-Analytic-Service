package com.brandanalytics.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank
        @Email
        @Size(max = 254, message = "Адрес электронной почты должен содержать от 8 до 100 элементов")
        String email,
        @NotBlank
        @Size(min = 8, max = 100, message = "Длина пароля должна находится в диапазоне от 8 до 100 символов")
        String password
) {
}
