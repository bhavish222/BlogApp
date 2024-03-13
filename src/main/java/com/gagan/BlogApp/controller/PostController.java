package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.User;
import com.gagan.BlogApp.service.PostService;
import com.gagan.BlogApp.service.PostServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

@Controller
@RequestMapping("/blog")
public class PostController {

    private PostService postService;

    public PostController(PostService thePostService) {
        postService = thePostService;
    }

    @GetMapping("/newpost")
    public String newPost(Model theModel) {

        // create model attribute to bind form data
        Post post = new Post();

        theModel.addAttribute("Post", post);

        return "newpost";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute("Post") Post post, Model theModel) {
        String content = post.getContent();
        String excerpt = content.length() > 30 ? content.substring(0, 30) : content;
        post.setExcerpt(excerpt);
        post.setIsPublished(true);
        post.setPublishedAt(new Timestamp(System.currentTimeMillis()));

        User user = new User(2,"Gagan","gagan@gmail.com", "1234");
        theModel.addAttribute("user" , user);

        postService.save(post);

        return "allPosts";
    }
}