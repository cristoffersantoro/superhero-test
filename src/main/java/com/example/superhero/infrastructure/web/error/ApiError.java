package com.example.superhero.infrastructure.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    public record FieldError(String field, String message) {}

    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    public static ApiError of(int status, String error, String message, String path, List<FieldError> fieldErrors) {
        ApiError e = new ApiError();
        e.timestamp = OffsetDateTime.now();
        e.status = status;
        e.error = error;
        e.message = message;
        e.path = path;
        e.fieldErrors = fieldErrors;
        return e;
    }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public List<FieldError> getFieldErrors() { return fieldErrors; }
}