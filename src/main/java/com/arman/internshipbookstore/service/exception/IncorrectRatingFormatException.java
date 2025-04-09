package com.arman.internshipbookstore.service.exception;

public class IncorrectRatingFormatException extends RuntimeException {
    public IncorrectRatingFormatException(String message) {
        super(message);
    }
}
