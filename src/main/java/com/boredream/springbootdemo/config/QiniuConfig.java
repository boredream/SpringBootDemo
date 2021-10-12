package com.boredream.springbootdemo.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfig {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String uploadHost;

    private String downloadHost;

    /**
     * 华东机房
     */
    @Bean
    public com.qiniu.storage.Configuration qnConfig() {
        return new com.qiniu.storage.Configuration(Region.region0());
    }

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qnConfig());
    }

    /**
     * 认证信息实例
     */
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qnConfig());
    }
}
