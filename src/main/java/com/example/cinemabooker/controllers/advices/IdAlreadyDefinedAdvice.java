package com.example.cinemabooker.controllers.advices;

import com.example.cinemabooker.services.exceptions.IdAlreadyDefinedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class IdAlreadyDefinedAdvice {
    @ResponseBody
    @ExceptionHandler(IdAlreadyDefinedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String entityNotFoundHandler(IdAlreadyDefinedException e) {
        return e.getMessage();
    }
}
