package com.boredream.springbootdemo.utils;


import com.boredream.springbootdemo.entity.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

public class MiscUtil {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(MiscUtil.class);

    static public BaseResponse<Map<String, String>> getValidateError(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) return null;

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getCode() + "|" + error.getDefaultMessage());
        }

        BaseResponse<Map<String, String>> ret = new BaseResponse<>(422, "输入错误"); //rfc4918 - 11.2. 422: Unprocessable Entity
        ret.setData(fieldErrors);

        return ret;
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
}

