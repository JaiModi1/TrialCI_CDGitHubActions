package com.digitallending.userservice.exception;

public class BusinessTypeNotExistsException extends RuntimeException {
    public BusinessTypeNotExistsException(String message) {
        super(message);
    }
}
