package com.portfolio.taskmanager.web.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldErrorItem(fe.getField(), fe.getDefaultMessage()));
        }
        ApiError body = new ApiError(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", errors,
                OffsetDateTime.now(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null;
            errors.add(new FieldErrorItem(field, cv.getMessage()));
        }
        ApiError body = new ApiError(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", errors,
                OffsetDateTime.now(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        ApiError body = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), List.of(),
                OffsetDateTime.now(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex, HttpServletRequest request) {
        // Log stacktrace to help diagnose issues like springdoc generation errors
        log.error("Unhandled exception at {} {}", request.getMethod(), request.getRequestURI(), ex);
        ApiError body = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor", List.of(),
                OffsetDateTime.now(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    public static class ApiError {
        private int statusCode;
        private String message;
        private List<FieldErrorItem> errors;
        private OffsetDateTime timestamp;
        private String path;

        public ApiError(int statusCode, String message, List<FieldErrorItem> errors, OffsetDateTime timestamp, String path) {
            this.statusCode = statusCode;
            this.message = message;
            this.errors = errors;
            this.timestamp = timestamp;
            this.path = path;
        }

        public int getStatusCode() { return statusCode; }
        public String getMessage() { return message; }
        public List<FieldErrorItem> getErrors() { return errors; }
        public OffsetDateTime getTimestamp() { return timestamp; }
        public String getPath() { return path; }

        public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
        public void setMessage(String message) { this.message = message; }
        public void setErrors(List<FieldErrorItem> errors) { this.errors = errors; }
        public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
        public void setPath(String path) { this.path = path; }
    }

    public static class FieldErrorItem {
        private String field;
        private String message;

        public FieldErrorItem(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public String getMessage() { return message; }

        public void setField(String field) { this.field = field; }
        public void setMessage(String message) { this.message = message; }
    }
}
