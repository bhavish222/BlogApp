package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.TagRepository;
import com.gagan.BlogApp.dao.UserRepository;
import com.gagan.BlogApp.entity.Role;
import com.gagan.BlogApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    @Override
    public User findByName(String name){
        return userRepository.findByName(name);
    }
    @Override
    public void saveUser(User user) {
        Role role = new Role();
        role.setRole("ROLE_AUTHOR");
        user.setRole(role);
        userRepository.save(user);
        role.setUser(user);
    }

    @Override
    public boolean isUserExists(String username) {
        User user = userRepository.findByName(username);

        return user != null;
    }
}
