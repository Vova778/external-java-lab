package com.epam.esm.exception.model;

public class ReceiptAlreadyExistsException extends RuntimeException{
    public ReceiptAlreadyExistsException() {
    }

    public ReceiptAlreadyExistsException(String message) {
        super(message);
    }

    public ReceiptAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}