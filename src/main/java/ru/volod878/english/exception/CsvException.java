package ru.volod878.english.exception;

public class CsvException extends RuntimeException {
    public CsvException(String message) {
        super(message);
    }

    public CsvException(ReflectiveOperationException exception) {
        super(exception);
    }
}
