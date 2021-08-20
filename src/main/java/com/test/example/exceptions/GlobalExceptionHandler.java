package com.test.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Invalid value")
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleValidationException() { }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Stack overflow")
    @ExceptionHandler(OutOfMemory.class)
    public void handleOutOfMemoryException() { }

    @ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Invalid value")
    @ExceptionHandler(InvalidSeed.class)
    public void handleInvalidSeedException() { }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Concurrency problem")
    @ExceptionHandler(ConcurrencyException.class)
    public void handleConcurencyException() { }

    @ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Too large value")
    @ExceptionHandler(SeedTooLarge.class)
    public void handleSeedTooLargeException() { }
}
