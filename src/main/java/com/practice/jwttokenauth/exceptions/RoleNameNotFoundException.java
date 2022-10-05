package com.practice.jwttokenauth.exceptions;

public class RoleNameNotFoundException extends RuntimeException {

    public RoleNameNotFoundException() {
        super();
    }

    public RoleNameNotFoundException(String message) {
        super(message);
    }
}
