package com.digitallending.userservice.exception;

public class RegexMisMatchException extends RuntimeException {
    public RegexMisMatchException(String message) {
        super(message);
    }
}