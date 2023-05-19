package com.example.cinemabooker.controllers.advices;

import com.example.cinemabooker.services.exceptions.BadReservationRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BadReservationRequestAdvice {
    @ResponseBody
    @ExceptionHandler(BadReservationRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badReservationRequestHandler(BadReservationRequestException e) {
        return e.getMessage();
    }
}
