package com.boredream.springbootdemo.repo;

import com.boredream.springbootdemo.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<UserInfo, Integer> {

}