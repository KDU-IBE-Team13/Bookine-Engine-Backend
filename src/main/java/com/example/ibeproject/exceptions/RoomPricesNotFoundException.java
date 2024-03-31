package com.example.ibeproject.exceptions;

public class RoomPricesNotFoundException extends RuntimeException {

    public RoomPricesNotFoundException(String message) {
        super(message);
    }

    public RoomPricesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
