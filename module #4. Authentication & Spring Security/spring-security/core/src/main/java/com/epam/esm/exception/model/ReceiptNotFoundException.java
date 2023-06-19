package com.epam.esm.exception.model;

public class ReceiptNotFoundException extends RuntimeException {
    public ReceiptNotFoundException() {
    }

    public ReceiptNotFoundException(String message) {
        super(message);
    }

    public ReceiptNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}