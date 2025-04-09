package com.arman.internshipbookstore.service.exception;

public class IncorrectPriceFormatException extends RuntimeException {
    public IncorrectPriceFormatException(String message) {
        super(message);
    }
}
