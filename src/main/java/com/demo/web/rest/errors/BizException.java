package com.demo.web.rest.errors;

public class BizException extends BaseException {

    public BizException(Integer code, String message) {
        super(code, message);
    }
}
