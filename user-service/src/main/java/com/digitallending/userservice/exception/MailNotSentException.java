package com.digitallending.userservice.exception;

public class MailNotSentException extends RuntimeException {
    public MailNotSentException(String message) {
        super(message);
    }

    public MailNotSentException(String message, Throwable err) {
        super(message, err);
    }
}
