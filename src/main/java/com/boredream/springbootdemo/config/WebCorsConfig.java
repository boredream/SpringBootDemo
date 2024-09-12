package com.boredream.springbootdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 设置映射
                .allowedOriginPatterns("*")        // 设置域
                .allowedMethods("*")               // 设置请求的方式GET、POST等
                .allowCredentials(true)            // 设置是否携带cookie
                .maxAge(3600)                      // 设置设置的有效期 秒单位
                .allowedHeaders("*");              // 设置头
    }
}