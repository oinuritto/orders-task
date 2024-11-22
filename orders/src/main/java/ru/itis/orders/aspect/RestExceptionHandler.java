package ru.itis.orders.aspect;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import ru.itis.orders.dto.ExceptionDto;
import ru.itis.orders.dto.ValidationErrorDto;
import ru.itis.orders.dto.ValidationErrorsDto;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsDto> handle(MethodArgumentNotValidException ex) {
        List<ValidationErrorDto> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ValidationErrorDto.builder()
                        .fieldName(error instanceof FieldError field
                                ? field.getField()
                                : error.getObjectName())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorsDto.builder()
                        .errors(errors)
                        .build());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionDto> handle(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build());
    }
}
