package com.example.ibeproject.exceptions;

public class RoomDetailsNotFoundException extends RuntimeException {

    public RoomDetailsNotFoundException(String message) {
        super(message);
    }

    public RoomDetailsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
