package com.example.superhero.infrastructure.web.error;

public class InvalidReferenceException extends RuntimeException {
    private final String field;
    private final java.util.Set<Long> invalidIds;

    public InvalidReferenceException(String field, java.util.Set<Long> invalidIds, String message) {
        super(message);
        this.field = field;
        this.invalidIds = java.util.Set.copyOf(invalidIds);
    }
    public String getField() { return field; }
    public java.util.Set<Long> getInvalidIds() { return invalidIds; }
}
