package com.example.simpleapi.exception;

public class OperationNotSupportedException extends RuntimeException {
    public OperationNotSupportedException(String message) {
        super(message);
    }
}
