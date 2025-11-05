package com.codechallenge.transactional_portal_api.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Username or password is incorrect");
    }
}
