package ru.shishmakov.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException ex, WebRequest request) {
        log.debug("error: " + ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = ArgumentException.class)
    public ResponseEntity<?> argumentException(ArgumentException ex, WebRequest request) {
        log.debug("error: " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> notFoundException(Exception ex, WebRequest request) {
        log.error("server error", ex);
        return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }
}
