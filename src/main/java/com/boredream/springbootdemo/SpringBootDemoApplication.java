package com.boredream.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "${project.name}") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/hello2")
    public String hello2(@RequestParam(value = "name", defaultValue = "${project.name}") String name) {
        return String.format("Hello2 %s!", name);
    }
}
