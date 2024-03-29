package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.boredream.springbootdemo.entity.BaseEntity;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        // https://baomidou.com/guide/generator-new.html
        FastAutoGenerator
                .create("jdbc:mysql://localhost:3306/love_cookbook",
                        "root",
                        "c06b4e42600d464b")
                .globalConfig(builder -> {
                    builder.author("boredream") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() // 禁止打开输出目录
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.boredream.springbootdemo") // 设置父包名
//                            .moduleName("system") // 设置父包模块名
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .controller("controller")
                            .other("other")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("trace_record", "trace_location"); // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateConfig(builder -> builder
//                        .disable(TemplateType.XML, TemplateType.CONTROLLER, TemplateType.SERVICE, TemplateType.SERVICEIMPL)
                        .controller("/templates/RestController.java")
                        .build())
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .strategyConfig(builder -> builder
                        .entityBuilder()
                        .superClass(BaseEntity.class)
                        .enableLombok()
                        .disableSerialVersionUID() // 禁用生成 serialVersionUID
                        .addIgnoreColumns("id", "createTime", "updateTime")
                        .build())
                .execute();
    }

}
