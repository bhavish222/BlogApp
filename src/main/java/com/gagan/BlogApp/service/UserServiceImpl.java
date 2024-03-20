package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.TagRepository;
import com.gagan.BlogApp.dao.UserRepository;
import com.gagan.BlogApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
