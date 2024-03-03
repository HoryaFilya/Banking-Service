package ru.shaikhraziev.bankingservice.exception;

public class IncorrectDataException extends RuntimeException {

    public IncorrectDataException(String message) {
        super(message);
    }
}