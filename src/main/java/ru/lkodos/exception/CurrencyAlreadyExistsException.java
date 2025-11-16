package ru.lkodos.exception;

public class CurrencyAlreadyExistsException extends RuntimeException {

    public CurrencyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}