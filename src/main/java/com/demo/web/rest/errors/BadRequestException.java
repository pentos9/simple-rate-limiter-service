package com.demo.web.rest.errors;

public class BadRequestException extends BaseException {
    public BadRequestException(String errorMessage) {
        super(400, errorMessage);
    }
}
