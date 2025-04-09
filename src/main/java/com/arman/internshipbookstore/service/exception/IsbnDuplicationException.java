package com.arman.internshipbookstore.service.exception;

public class IsbnDuplicationException extends RuntimeException {
    public IsbnDuplicationException(String message){
        super(message);
    }
}
