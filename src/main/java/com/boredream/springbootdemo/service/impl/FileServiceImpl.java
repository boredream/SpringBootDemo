package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.config.QiniuConfig;
import com.boredream.springbootdemo.entity.dto.FileUploadPolicyDTO;
import com.boredream.springbootdemo.service.IFileService;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private Auth auth;

    @Autowired
    private QiniuConfig qiniuConfig;

    @Override
    public FileUploadPolicyDTO getUploadPolicy() {
        FileUploadPolicyDTO dto = new FileUploadPolicyDTO();
        dto.setUploadHost(qiniuConfig.getUploadHost());
        dto.setDownloadHost(qiniuConfig.getDownloadHost());
        dto.setToken(auth.uploadToken(qiniuConfig.getBucket()));
        return dto;
    }
}
