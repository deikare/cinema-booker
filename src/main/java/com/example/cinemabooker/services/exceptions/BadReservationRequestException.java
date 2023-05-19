package com.example.cinemabooker.services.exceptions;

public class BadReservationRequestException extends RuntimeException {
    public BadReservationRequestException(String message) {
        super(message);
    }
}
