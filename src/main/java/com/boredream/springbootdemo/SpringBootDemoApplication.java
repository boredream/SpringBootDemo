package com.boredream.springbootdemo;

import com.boredream.springbootdemo.entity.UserInfo;
import com.boredream.springbootdemo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/login")
    public String login(@RequestBody UserInfo info) {
        // FIXME: 2021/9/2 
        return "login";
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@RequestBody UserInfo info) {
        userRepository.save(info);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }


}
