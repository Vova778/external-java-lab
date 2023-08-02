package com.epam.esm.exception.model;

public class GiftCertificateAlreadyExistsException extends RuntimeException {
    public GiftCertificateAlreadyExistsException() {
    }

    public GiftCertificateAlreadyExistsException(String message) {
        super(message);
    }

    public GiftCertificateAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}