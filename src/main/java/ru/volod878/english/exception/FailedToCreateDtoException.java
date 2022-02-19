package ru.volod878.english.exception;

public class FailedToCreateDtoException extends RuntimeException {
    public FailedToCreateDtoException(String message) {
        super(message);
    }
}