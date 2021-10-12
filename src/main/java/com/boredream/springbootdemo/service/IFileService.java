package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.dto.FileUploadPolicyDTO;

public interface IFileService {

    /**
     * 获取前端上传凭证
     */
    FileUploadPolicyDTO getUploadPolicy();

}
