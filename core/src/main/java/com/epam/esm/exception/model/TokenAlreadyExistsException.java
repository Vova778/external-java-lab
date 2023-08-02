package com.epam.esm.exception.model;

public class TokenAlreadyExistsException extends RuntimeException {
    public TokenAlreadyExistsException() {}

    public TokenAlreadyExistsException(String message) {
        super(message);
    }

    public TokenAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}