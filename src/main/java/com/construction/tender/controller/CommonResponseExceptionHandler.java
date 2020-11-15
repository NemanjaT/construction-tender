package com.construction.tender.controller;

import com.construction.tender.dto.response.ErrorResponse;
import com.construction.tender.exception.IdNotForCallerException;
import com.construction.tender.exception.InvalidIdProvidedException;
import com.construction.tender.exception.InvalidOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@RestController
@RestControllerAdvice
@Slf4j
public class CommonResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Validation error in rest API", ex);
        final var errors = new ArrayList<String>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(fieldError.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(globalError -> errors.add(globalError.getDefaultMessage()));
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        1001,
                        "Validation failed.",
                        String.join(". ", errors))
        );
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Object> handleInvalidOperationException(InvalidOperationException e, WebRequest webRequest) {
        log.error("Invalid operation exception in rest API", e);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        1002,
                        "Invalid operation.",
                        e.getMessage())
        );
    }

    @ExceptionHandler(InvalidIdProvidedException.class)
    public ResponseEntity<Object> handleInvalidIdProvidedException(InvalidIdProvidedException e, WebRequest webRequest) {
        log.error("Invalid id provided in rest API", e);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        1003,
                        "Invalid ID provided.",
                        e.getMessage())
        );
    }

    @ExceptionHandler(IdNotForCallerException.class)
    public ResponseEntity<Object> handleIdNotForCallerException(IdNotForCallerException e, WebRequest webRequest) {
        log.error("ID not for caller in rest API", e);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        1004,
                        "ID not for caller.",
                        e.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> invalidArgumentExceptionHandler(IllegalArgumentException e, WebRequest webRequest) {
        log.error("Invalid operation exception in rest API", e);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        1005,
                        "Illegal argument.",
                        e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception e, WebRequest webRequest) {
        log.error("Unexpected error in rest API", e);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        1000,
                        "Internal server error.",
                        e.getMessage())
        );
    }
}
