package com.digitallending.userservice.exception;

public class WrongAttributeValueException extends RuntimeException{
    public WrongAttributeValueException(String message)
    {
        super(message);
    }
}
