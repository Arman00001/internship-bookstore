package com.arman.internshipbookstore.service.exception;

public class InvalidPublicationDateException extends RuntimeException {
    public InvalidPublicationDateException(String message) {
        super(message);
    }
}
