package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByName(String name);
    boolean isUserExists(String username);
    void saveUser(User user);
}
