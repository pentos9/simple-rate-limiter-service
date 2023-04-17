package com.demo.web.rest.errors;

import com.demo.web.rest.common.Constants;

public class TooManyRequestException extends BizException {

    public TooManyRequestException() {
        super(Constants.TOO_MANY_REQUEST, "Too Many Request");
    }

    public TooManyRequestException(String message) {
        super(Constants.TOO_MANY_REQUEST, message);
    }
}
