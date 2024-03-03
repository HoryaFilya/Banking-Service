package ru.shaikhraziev.bankingservice.exception;

public class DeletingLastElementException extends RuntimeException {

    public DeletingLastElementException(String message) {
        super(message);
    }
}