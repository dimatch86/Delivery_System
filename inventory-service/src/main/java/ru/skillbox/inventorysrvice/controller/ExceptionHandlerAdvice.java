package ru.skillbox.inventorysrvice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.dto.ErrorDto;
import ru.skillbox.inventorysrvice.errors.ProductNotFoundException;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final String UNIQUE_VIOLATION = "23505";

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> exceptionHandler(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> exceptionHandler1(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> exceptionHandler1(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDto> sqlExceptionHandler(SQLException ex) {
        if (ex.getSQLState().equals(UNIQUE_VIOLATION)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("Product with this name already exists", LocalDateTime.now()));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Internal service error", LocalDateTime.now()));
    }
}
