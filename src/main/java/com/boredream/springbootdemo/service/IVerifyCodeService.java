package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.VerifyCode;

public interface IVerifyCodeService {

    VerifyCode sendVerifyCode(String phone, Integer verifyType);

    boolean checkVerifyCode(VerifyCode verifyCode);

}
