package ru.nsu.projects.malaeva.core;

public class OperationNotSupportedException extends RuntimeException {
    public OperationNotSupportedException(String message) {
        super(message);
    }
}
