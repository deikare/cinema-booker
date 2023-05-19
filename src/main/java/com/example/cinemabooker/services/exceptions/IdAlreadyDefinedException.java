package com.example.cinemabooker.services.exceptions;

public class IdAlreadyDefinedException extends RuntimeException {
    public IdAlreadyDefinedException(String message) {
        super(message);
    }
}
