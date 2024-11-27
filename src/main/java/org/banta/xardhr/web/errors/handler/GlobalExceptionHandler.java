package org.banta.xardhr.web.errors.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Data;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.banta.xardhr.web.errors.exception.JwtException;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Data
    @Builder
    public static class ErrorResponse {
        private HttpStatus status;
        private LocalDateTime timestamp;
        private String message;
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce((m1, m2) -> m1 + "; " + m2)
                .orElse("Validation failed");
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Malformed JSON request")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        return buildResponseEntity(
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("An unexpected error occurred")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}