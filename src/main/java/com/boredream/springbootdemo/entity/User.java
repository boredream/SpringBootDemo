package com.boredream.springbootdemo.entity;

import lombok.Data;

@Data
public class User extends BaseEntity {

    private Integer id;
    private String username;
    private String password;
    private String role;
    private String openId;

}
