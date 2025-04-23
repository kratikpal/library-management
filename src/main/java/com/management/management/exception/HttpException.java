package com.management.management.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    private final HttpStatus status;

    public HttpException() {
        super("Internal Server Error");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return super.getMessage();
    }

    public String getStatusCode() {
        return String.valueOf(status.value());
    }
}
