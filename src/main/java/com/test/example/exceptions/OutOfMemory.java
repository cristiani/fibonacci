package com.test.example.exceptions;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class OutOfMemory extends RuntimeException {
    private static final long serialVersionUID = 363676231345643L;
    private String message;
    public OutOfMemory( String message) {
        this.message = message;
    }
}
