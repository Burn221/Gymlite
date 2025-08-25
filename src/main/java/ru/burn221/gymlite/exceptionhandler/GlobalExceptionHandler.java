package ru.burn221.gymlite.exceptionhandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.burn221.gymlite.exceptions.ConflictException;
import ru.burn221.gymlite.exceptions.NotFoundException;

import java.util.Map;


//todo
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> illegalArgumentHandler(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String,String>> notFoundHandler(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String,String>> conflictHandker(ConflictException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error",ex.getMessage()));
    }
}
