package ru.shaikhraziev.bankingservice.exception;

public class NotEnoughRightsException extends RuntimeException {

    public NotEnoughRightsException(String message) {
        super(message);
    }
}