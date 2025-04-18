package com.arman.internshipbookstore.service.exception;

public class CharacterAlreadyExistsException extends RuntimeException {
    public CharacterAlreadyExistsException(String message) {
        super(message);
    }
}
