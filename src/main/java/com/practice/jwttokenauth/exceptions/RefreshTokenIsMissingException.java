package com.practice.jwttokenauth.exceptions;

public class RefreshTokenIsMissingException extends RuntimeException {
    public RefreshTokenIsMissingException() {
        super();
    }

    public RefreshTokenIsMissingException(String message) {
        super(message);
    }
}
