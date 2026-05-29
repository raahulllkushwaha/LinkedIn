package com.rahul.userservice.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message, String email) {
        super(message);
    }
}
