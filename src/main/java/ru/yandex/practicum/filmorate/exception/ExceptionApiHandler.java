package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {
    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<String> validateException(ValidateException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.status(500).body(exception.getMessage());
    }
}
