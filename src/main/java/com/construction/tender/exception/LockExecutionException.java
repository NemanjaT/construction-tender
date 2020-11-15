package com.construction.tender.exception;

public class LockExecutionException extends RuntimeException {
    public LockExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockExecutionException(Throwable cause) {
        super(cause);
    }
}
