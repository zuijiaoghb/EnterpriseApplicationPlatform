package com.enterprise.platform.system.exception;

public class DuplicateClientException extends RuntimeException {
    public DuplicateClientException(String message) {
        super(message);
    }
}
