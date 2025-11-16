package ru.lkodos.exception;

public class DbAccessException extends RuntimeException {

    public DbAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}