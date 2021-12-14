package com.boredream.springbootdemo.service;

public interface IVerifyCodeService {

    String sendVerifyCode(String phone, long duration, boolean mock);

    boolean checkVerifyCode(String phone, String code);

}
