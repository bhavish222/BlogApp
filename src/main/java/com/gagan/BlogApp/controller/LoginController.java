package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.User;
import com.gagan.BlogApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/chalgya")
    public String test() {
        return "redirect:/allposts";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "Register";
    }

    @PostMapping("/register")
    public String processNewUser(@ModelAttribute("user") User user, Model model) {
        String username = user.getName();
        if (userService.isUserExists(username)) {
            model.addAttribute("error", "Username already exists");
            return "Register";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
