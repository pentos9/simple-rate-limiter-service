package com.demo.web.rest.errors;

import lombok.Data;

@Data
public abstract class BaseException extends RuntimeException {
    private int errorCode;
    private String errorMessage;

    public BaseException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
