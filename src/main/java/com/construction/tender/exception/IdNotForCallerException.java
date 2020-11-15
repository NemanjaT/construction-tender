package com.construction.tender.exception;

public class IdNotForCallerException extends InvalidOperationException {
    public IdNotForCallerException(String message) {
        super(message);
    }

    public IdNotForCallerException(String message, Throwable cause) {
        super(message, cause);
    }
}
