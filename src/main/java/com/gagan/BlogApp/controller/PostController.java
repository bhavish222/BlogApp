package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.User;
import com.gagan.BlogApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService thePostService) {
        postService = thePostService;
    }

    @GetMapping("/newpost")
    public String newPost(Model theModel) {
        Post post = new Post();
        theModel.addAttribute("Post", post);
        return "newpost";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute("Post") Post post, Model model) {
        if(post.getId()==0)
        {
            String content = post.getContent();
            String excerpt = content.length() > 30 ? content.substring(0, 30) : content;
            post.setExcerpt(excerpt);
            post.setIsPublished(true);
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            User user = new User(1, "Gagan", "gagan@gmail.com", "1234");
            model.addAttribute("user", user);
            post.setAuthor(user);
            postService.save(post);
        }
        else{
            Post existingPost = postService.findById(post.getId());
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            postService.save(post);
        }

        return "redirect:/blog/allposts";
    }

    @GetMapping("/allposts")
    public String allPosts(Model theModel) {

        // create model attribute to bind form data
        List<Post> posts = postService.findAll();
        System.out.println(posts);

        theModel.addAttribute("posts", posts);

        return "allPosts";
    }

    @GetMapping("/post/{postId}")
    public String post(@PathVariable("postId") int postId, Model theModel) {
        Post post = postService.findById(postId);
        theModel.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/update/{postId}")
    public String update(@PathVariable("postId") int postId, Model theModel) {
        // Use the postService to find the post by its ID
        Post post = postService.findById(postId);
        theModel.addAttribute("Post", post);
        return "newpost";
    }

    @GetMapping("/delete/{postId}")
    public String delete(@PathVariable("postId") int theId) {


        // delete the employee
        postService.deleteById(theId);

        // redirect to /employees/list
        return "redirect:/blog/allposts";

    }


}