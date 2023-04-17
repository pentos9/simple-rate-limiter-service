package com.demo.web.rest.errors;

import com.demo.web.rest.common.Constants;

public class BizException extends BaseException {

    public BizException(Integer code, String message) {
        super(Constants.TOO_MANY_REQUEST, message);
    }

    public BizException(String message) {
        super(Constants.TOO_MANY_REQUEST, "biz exception");
    }
}
