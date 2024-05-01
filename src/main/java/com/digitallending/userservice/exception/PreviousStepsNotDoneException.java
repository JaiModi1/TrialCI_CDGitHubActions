package com.digitallending.userservice.exception;

public class PreviousStepsNotDoneException extends RuntimeException {
    public PreviousStepsNotDoneException(String message) {
        super(message);
    }
}
