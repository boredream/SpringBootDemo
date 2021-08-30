package com.boredream.springbootdemo.mapper;

import com.boredream.springbootdemo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper//指定这是一个操作数据库的mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User findUser(String username);

    @Insert("insert into user(username,password) values (#{username},#{password})")
    void insert(User user);

}
