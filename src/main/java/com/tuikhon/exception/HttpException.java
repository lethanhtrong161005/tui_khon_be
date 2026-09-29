package com.tuikhon.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom runtime exception carrying HTTP status code and error message.
 */
@Getter
public class HttpException extends RuntimeException {

    private final int statusCode;

    /**
     * Constructs HttpException with HttpStatus enum and error message.
     *
     * @param status  HTTP status enum.
     * @param message Human-readable error message.
     */
    public HttpException(HttpStatus status, String message) {
        this(status.value(), message);
    }

    /**
     * Constructs HttpException with status integer and error message.
     *
     * @param statusCode HTTP status integer.
     * @param message    Human-readable error message.
     */
    public HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
