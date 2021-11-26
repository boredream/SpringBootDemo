package com.boredream.springbootdemo.handler;

import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [ 全局异常拦截 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Slf4j
@ControllerAdvice
public class SmartGlobalExceptionHandler {

    /**
     * 添加全局异常处理流程
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseDTO<?> exceptionHandler(Exception e) {
        log.error("error:", e);

        // http 请求方式错误
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseDTO.error("请求方式错误");
        }

        // 参数类型错误
        if (e instanceof TypeMismatchException) {
            String errorInfo = ((TypeMismatchException) e).getPropertyName();
            return ResponseDTO.error("参数异常 " + errorInfo);
        }

        // json 格式错误
        if (e instanceof HttpMessageNotReadableException) {
            String errorInfo = e.getMessage();
            return ResponseDTO.error("Json格式错误 " + errorInfo);
        }

        // Sql 错误
        if (e instanceof SQLSyntaxErrorException) {
            String errorInfo = e.getMessage();
            return ResponseDTO.error("SQL错误 " + errorInfo);
        }

        // 参数校验未通过
        if (e instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
            return ResponseDTO.error("参数格式错误 " + String.join(",", msgList));
        }

        return ResponseDTO.error(e.getMessage());
    }
}
