package com.boredream.springbootdemo.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(BaseResponse.class);

    private int code = -1;
    private String message = "待处理";
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
