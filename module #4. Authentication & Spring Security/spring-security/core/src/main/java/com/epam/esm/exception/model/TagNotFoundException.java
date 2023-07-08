package com.epam.esm.exception.model;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}