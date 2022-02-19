package ru.volod878.english.exception;

public class FailedAudioStreamingException extends RuntimeException {
    public FailedAudioStreamingException(String message) {
        super(message);
    }
}