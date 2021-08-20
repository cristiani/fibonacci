package com.test.example.exceptions;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Should never be thrown because of the controller validation constraints.
 */
@Getter
@Setter
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidSeed extends RuntimeException {
    private static final long serialVersionUID = 363456345643L;
    private String message;
    public InvalidSeed( String message) {
        this.message = message;
    }
}

