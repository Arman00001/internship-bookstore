package com.arman.internshipbookstore.service.exception;

public class AwardNotFoundException extends RuntimeException {
    public AwardNotFoundException(String message) {
        super(message);
    }
}
