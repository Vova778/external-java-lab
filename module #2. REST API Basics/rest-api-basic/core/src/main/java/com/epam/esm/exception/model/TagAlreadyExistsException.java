package com.epam.esm.exception.model;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException() {
    }

    public TagAlreadyExistsException(String message) {
        super(message);
    }

    public TagAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}