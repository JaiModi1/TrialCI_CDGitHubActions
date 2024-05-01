package com.digitallending.userservice.exception;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message)
    {
        super(message);
    }
}
