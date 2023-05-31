package com.socialmedia.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchResourceExistException(ResourceExistsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.CONFLICT.value(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    private ResponseEntity<ApplicationError> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> handleAllOtherExceptions(RuntimeException e) {
        log.error(e.toString(), e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now() + " Local server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
