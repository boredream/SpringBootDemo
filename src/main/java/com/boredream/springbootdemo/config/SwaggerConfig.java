package com.boredream.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket getUserDocket() {
        // http://localhost:8080/api/swagger-ui/index.html#/
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("接口文档")//api标题
                .description("描述")//api描述
                .version("1.0.0")//版本号
                .build();

        return new Docket(DocumentationType.SWAGGER_2)//文档类型（swagger2）
                .apiInfo(apiInfo)//设置包含在json ResourceListing响应中的api元信息
                .select()//启动用于api选择的构建器
                .apis(RequestHandlerSelectors.basePackage("com.boredream.springbootdemo.controller"))//扫描接口的包
                .build();
    }
}