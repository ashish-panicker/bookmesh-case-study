package com.example.bookservice.exceptions;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String formatted) {
        super(formatted);
    }
}
