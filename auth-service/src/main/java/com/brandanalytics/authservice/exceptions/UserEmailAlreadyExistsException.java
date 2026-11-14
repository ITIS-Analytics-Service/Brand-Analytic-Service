package com.brandanalytics.authservice.exceptions;

public class UserEmailAlreadyExistsException extends RuntimeException {

    public UserEmailAlreadyExistsException() {
        super("Пользователь с таким email уже существует");
    }
}
