package com.boredream.springbootdemo.exception;

import com.boredream.springbootdemo.entity.BaseResponse;

public class ApiException extends RuntimeException {

    private BaseResponse<?> baseResponse;

    public ApiException(String message) {
        this(100, message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.baseResponse = new BaseResponse<>(code, message);
    }

    public BaseResponse<?> getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse<?> baseResponse) {
        this.baseResponse = baseResponse;
    }
}
