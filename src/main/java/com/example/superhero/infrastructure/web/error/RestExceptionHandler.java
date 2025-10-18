package com.example.superhero.infrastructure.web.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var locale = request.getLocale();
        var fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(
                        fe.getField(),
                        messageSource.getMessage(fe, locale)))
                .collect(Collectors.toList());

        var body = ApiError.of(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                messageSource.getMessage("erro.validacao", null, "Validation error", locale),
                request.getDescription(false).replace("uri=", ""),
                fields
        );
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req, Locale locale) {
        var fields = ex.getConstraintViolations().stream()
                .map(cv -> new ApiError.FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
        var body = ApiError.of(422, "Unprocessable Entity",
                messageSource.getMessage("erro.validacao", null, "Validation error", locale),
                req.getRequestURI(), fields);
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, HttpServletRequest req, Locale locale) {
        var msg = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);
        var body = ApiError.of(404, "Not Found", msg, req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex, HttpServletRequest req, Locale locale) {
        var body = ApiError.of(500, "Internal Server Error",
                messageSource.getMessage("erro.generico", null, "Unexpected error", locale),
                req.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
