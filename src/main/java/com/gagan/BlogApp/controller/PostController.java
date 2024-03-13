package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class PostController {

    @GetMapping("/newpost")
    public String newPost(Model theModel) {

        // create model attribute to bind form data
        Post post = new Post();

        theModel.addAttribute("Post", post);

        return "newpost";
    }

    @PostMapping("/savepost")
    public String savePost(Model theModel) {


        //PostService.save(post);

        return "allPosts";
    }
}