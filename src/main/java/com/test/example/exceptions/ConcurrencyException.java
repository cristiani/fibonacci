package com.test.example.exceptions;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ConcurrencyException extends RuntimeException {
    private static final long serialVersionUID = 4466343242345165643L;
    private String message;
    public ConcurrencyException( String message) {
        this.message = message;
    }
}

