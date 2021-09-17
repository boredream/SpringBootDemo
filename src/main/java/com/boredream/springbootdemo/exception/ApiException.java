package com.boredream.springbootdemo.exception;

import com.boredream.springbootdemo.entity.ResponseDTO;

public class ApiException extends RuntimeException {

    private ResponseDTO baseResponse;

    public ApiException(String message) {
        this(100, message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.baseResponse = new ResponseDTO();
        this.baseResponse.setCode(code);
        this.baseResponse.setMsg(message);
    }

    public ResponseDTO getResponseDTO() {
        return baseResponse;
    }

    public void setResponseDTO(ResponseDTO baseResponse) {
        this.baseResponse = baseResponse;
    }
}
