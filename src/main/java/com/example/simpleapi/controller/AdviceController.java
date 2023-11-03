package com.example.simpleapi.controller;

import com.example.simpleapi.exception.NotFoundException;
import com.example.simpleapi.exception.OperationNotSupportedException;
import com.example.simpleapi.model.dto.ExceptionDTO;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> notFound(NotFoundException exception, HttpServletRequest request) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.NOT_FOUND.value(), Instant.now(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ExceptionDTO> dateTimeParse(DateTimeParseException exception, HttpServletRequest request) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionDTO);
    }

    @ExceptionHandler(JsonPatchException.class)
    public ResponseEntity<ExceptionDTO> operationNotSupported(JsonPatchException exception, HttpServletRequest request) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionDTO);
    }
}
