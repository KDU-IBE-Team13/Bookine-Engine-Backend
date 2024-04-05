package com.example.ibeproject.exceptions;

public class InsufficientRoomsException extends RuntimeException {

    public InsufficientRoomsException(String message) {
        super(message);
    }

    public InsufficientRoomsException(String message, Throwable cause) {
        super(message, cause);
    }
}
