package com.digitallending.userservice.exception;

public class DetailsNotFoundException extends RuntimeException {
    public DetailsNotFoundException(String message) {
        super(message);
    }

    public DetailsNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
