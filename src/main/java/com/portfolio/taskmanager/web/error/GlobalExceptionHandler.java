package com.portfolio.taskmanager.web.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldErrorItem(fe.getField(), fe.getDefaultMessage()));
        }
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos");
        pd.setTitle("Validation Failed");
        pd.setProperty("errors", errors);
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldErrorItem> errors = new ArrayList<>();
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null;
            errors.add(new FieldErrorItem(field, cv.getMessage()));
        }
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos");
        pd.setTitle("Constraint Violation");
        pd.setProperty("errors", errors);
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Not Found");
        pd.setProperty("errors", List.of());
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Invalid Argument");
        pd.setProperty("errors", List.of());
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
        
        log.error("Unhandled exception at {} {}", request.getMethod(), request.getRequestURI(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        pd.setTitle("Internal Server Error");
        pd.setProperty("errors", List.of());
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
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
