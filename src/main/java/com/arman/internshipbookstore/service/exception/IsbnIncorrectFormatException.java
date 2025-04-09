package com.arman.internshipbookstore.service.exception;

public class IsbnIncorrectFormatException extends RuntimeException {
    public IsbnIncorrectFormatException(String message){
        super(message);
    }
}
