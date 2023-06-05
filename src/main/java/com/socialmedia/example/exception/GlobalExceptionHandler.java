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
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApplicationError> catchValidationException(ValidationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TimeException.class)
    public ResponseEntity<ApplicationError> catchValidationException(TimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ApplicationError> catchFileException(FileException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(AppAuthenticationException.class)
    public ResponseEntity<ApplicationError> catchAppAuthenticationException(AppAuthenticationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ApplicationError> catchResourceExistException(ResourceExistsException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.CONFLICT.value(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ApplicationError> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApplicationError> handleAccessDeniedException(Exception e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new ApplicationError(HttpStatus.FORBIDDEN.value(), e.getMessage()),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IndexOutOfBoundsException.class,
                        NumberFormatException.class, ArithmeticException.class, ArrayIndexOutOfBoundsException.class,})
    public ResponseEntity<ApplicationError> handleAllOtherExceptions(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now() + " " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
