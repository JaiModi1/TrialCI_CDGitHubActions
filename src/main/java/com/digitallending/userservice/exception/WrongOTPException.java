package com.digitallending.userservice.exception;

public class WrongOTPException extends RuntimeException {
    public WrongOTPException(String message) {
        super(message);
    }

    public WrongOTPException(String message, Throwable err) {
        super(message, err);
    }
}