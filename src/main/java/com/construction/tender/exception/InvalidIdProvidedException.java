package com.construction.tender.exception;

public class InvalidIdProvidedException extends InvalidOperationException {
    public InvalidIdProvidedException(String message) {
        super(message);
    }

    public InvalidIdProvidedException(String message, Throwable cause) {
        super(message, cause);
    }
}
