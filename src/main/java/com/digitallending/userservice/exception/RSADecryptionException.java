package com.digitallending.userservice.exception;

public class RSADecryptionException extends RuntimeException {
    public RSADecryptionException(String message) {
        super(message);
    }
}
