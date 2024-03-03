package ru.shaikhraziev.bankingservice.exception;

public class InvalidLoginOrPasswordException extends RuntimeException {

    public InvalidLoginOrPasswordException(String message) {
        super(message);
    }
}