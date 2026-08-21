package com.example.bookorderservice.exception;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String formatted) {
        super(formatted);
    }
}
