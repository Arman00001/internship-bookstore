package com.arman.internshipbookstore.service.exception;

public class AwardAlreadyExistsException extends RuntimeException {
    public AwardAlreadyExistsException(String message) {
        super(message);
    }
}
