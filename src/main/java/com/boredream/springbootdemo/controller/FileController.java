package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.dto.FileUploadPolicyDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private IFileService service;

    @GetMapping(value = "/getUploadPolicy", produces = "application/json")
    public ResponseDTO<FileUploadPolicyDTO> getUploadPolicy() {
        return ResponseDTO.succData(service.getUploadPolicy());
    }

}
