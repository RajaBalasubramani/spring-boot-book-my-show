package com.driver.bookMyShow.Exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus; // Import the HttpStatus class

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnderageException extends RuntimeException {
    public UnderageException(String message) {
        super(message);
    }
}
