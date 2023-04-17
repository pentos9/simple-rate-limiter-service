package com.demo.web.rest.common;

import com.demo.web.rest.errors.BadRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleException(Exception ex) {
        log.error("handleException error message:{}", ex.getMessage());
        ErrorResult errorResult = new ErrorResult(500, ex.getMessage());
        return errorResult;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleBadRequestException(BadRequestException ex) {
        log.error("handleBadRequestException error message:{}", ex.getMessage());
        ErrorResult errorResult = new ErrorResult(ex.getErrorCode(), ex.getMessage());
        return errorResult;
    }

    public static final class ErrorResult {
        @JsonProperty("error_code")
        private Integer code;
        @JsonProperty("error_message")
        private String message;

        public ErrorResult(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
