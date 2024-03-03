package ru.shaikhraziev.bankingservice.handler;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.shaikhraziev.bankingservice.error.ApiError;
import ru.shaikhraziev.bankingservice.exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestControllerAdvice(basePackages = "ru.shaikhraziev.bankingservice.controller")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private String DATE = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message("Malformed JSON Request")
                .build();

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message("Method Argument Not Valid")
                .errors(errors)
                .build();

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                        ex.getPropertyName(), ex.getValue(), ex.getRequiredType().getSimpleName()))
                .build();

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message("No Handler Found")
                .build();

        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleDuplicateExceptions(DataIntegrityViolationException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message("Duplicate key exception: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleResourceNotFoundExceptions(ResourceNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleIncorrectDataExceptions(IncorrectDataException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleDeletingLastElementExceptions(DeletingLastElementException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleNotEnoughRightsExceptions(NotEnoughRightsException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleInsufficientFundsExceptions(InsufficientFundsException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleInvalidLoginOrPasswordExceptions(InvalidLoginOrPasswordException ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
        ApiError apiError = ApiError.builder()
                .date(DATE)
                .message("Error: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}