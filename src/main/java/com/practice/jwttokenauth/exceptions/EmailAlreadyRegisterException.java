package com.practice.jwttokenauth.exceptions;

public class EmailAlreadyRegisterException extends RuntimeException {

    public EmailAlreadyRegisterException() {
        super();
    }

    public EmailAlreadyRegisterException(String message) {
        super(message);
    }
}
