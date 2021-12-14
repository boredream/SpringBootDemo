package com.boredream.springbootdemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

// http://sms.shansuma.com/docs
@Data
@NoArgsConstructor
public class ShansumaResponse {

    private Integer code;
    private String message;
    private DataDTO data;

    @Data
    @NoArgsConstructor
    public static class DataDTO {
        private Integer code;
        private String message;
    }
}
