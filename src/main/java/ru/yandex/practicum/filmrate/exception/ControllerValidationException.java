package ru.yandex.practicum.filmrate.exception;

public class ControllerValidationException extends RuntimeException {
    public ControllerValidationException(String message) {
        super(message);
    }
}
