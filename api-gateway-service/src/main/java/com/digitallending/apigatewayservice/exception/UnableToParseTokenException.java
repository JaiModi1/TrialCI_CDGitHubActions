package com.digitallending.apigatewayservice.exception;

public class UnableToParseTokenException extends RuntimeException {
    public UnableToParseTokenException(String message) {
        super(message);
    }
}