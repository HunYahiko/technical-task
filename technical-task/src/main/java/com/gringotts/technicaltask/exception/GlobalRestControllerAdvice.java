package com.gringotts.technicaltask.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
class GlobalRestControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handle(EntityNotFoundException exception) {
        return ResponseEntity.badRequest().body(getErrorsMap(List.of(exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handle(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(getErrorsMap(exception.getBindingResult()
                                                                      .getFieldErrors()
                                                                      .stream()
                                                                      .map(FieldError::getDefaultMessage)
                                                                      .toList()));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        return Map.of("errors", errors);
    }

    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<Map<String, List<String>>> handle(InvalidPostException exception) {
        return ResponseEntity.badRequest().body(getErrorsMap(List.of(exception.getMessage())));
    }
}
