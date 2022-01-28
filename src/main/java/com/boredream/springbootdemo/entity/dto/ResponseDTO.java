package com.boredream.springbootdemo.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_COMMON_ERROR = 100;

    protected Integer code;

    protected String msg;

    protected Boolean success;

    protected T data;

    public static <T> ResponseDTO<T> success(T data) {
        return success("请求成功", data);
    }

    public static <T> ResponseDTO<T> success(String msg, T data) {
        return new ResponseDTO<>(CODE_SUCCESS, msg, true, data);
    }

    public static <T> ResponseDTO<T> error(String msg) {
        return error(CODE_COMMON_ERROR, msg, null);
    }

    public static <T> ResponseDTO<T> error(Integer code, String msg, T data) {
        return new ResponseDTO<>(code, msg, false, data);
    }

    // 错误封装

    public static <T> ResponseDTO<T> errorMsgSecCheck() {
        return new ResponseDTO<>(200, "内容包含敏感字符，请重新编辑后发送", false, null);
    }

    @Override
    public String toString() {
        return "ResponseDTO{" + "code=" + code + ", msg='" + msg + '\'' + ", success=" + success + ", data=" + data +
                '}';
    }
}
