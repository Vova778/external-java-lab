package com.epam.esm.exception.model;

public class GiftCertificateNotFoundException extends RuntimeException {
    public GiftCertificateNotFoundException() {
    }

    public GiftCertificateNotFoundException(String message) {
        super(message);
    }

    public GiftCertificateNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}