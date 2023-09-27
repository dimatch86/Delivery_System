package ru.skillbox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.dto.ErrorDto;
import ru.skillbox.errors.ShipmentNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {


    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ErrorDto> exceptionHandler(ShipmentNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage(), LocalDateTime.now()));
    }
}
