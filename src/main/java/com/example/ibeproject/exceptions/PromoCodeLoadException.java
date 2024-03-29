package com.example.ibeproject.exceptions;

import java.sql.SQLException;

public class PromoCodeLoadException extends SQLException {

    public PromoCodeLoadException(String message) {
        super(message);
    }

    public PromoCodeLoadException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
