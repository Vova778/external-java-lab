package com.epam.esm.exception.model;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {}

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }
}