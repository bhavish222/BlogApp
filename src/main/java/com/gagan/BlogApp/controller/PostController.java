package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.entity.User;
import com.gagan.BlogApp.service.PostService;
import com.gagan.BlogApp.service.TagService;
import com.gagan.BlogApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private PostService postService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/newpost")
    public String newPost(Model theModel) {
        Post post = new Post();
        theModel.addAttribute("Post", post);
        return "newpost";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute("Post") Post post, @RequestParam("tagString") String tagString, Model model, Authentication authentication) {
        postService.save(post, tagString, authentication);
        return "redirect:/allposts";
    }

    @GetMapping("/allposts")
    public String allPosts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                           @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                           @RequestParam(value = "field", required = false) String searchField,
                           @RequestParam(value = "selectedOption", defaultValue = "desc", required = false) String selectedOption,
                           @RequestParam(value = "authors", required = false) List<String> authors,
                           @RequestParam(value = "tags", required = false) List<String> filterTags,
                           @RequestParam(value = "startDate", required = false) String startDate,
                           @RequestParam(value = "endDate", required = false) String endDate,
                           Model model) {
        System.out.println(searchField);
        System.out.println(selectedOption);
        System.out.println(authors);
        System.out.println(filterTags);
        System.out.println(startDate);
        System.out.println(endDate);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Post> posts = postService.findposts(searchField, selectedOption, authors, filterTags, startDate, endDate, pageable);

        List<Tag> tags = tagService.findAllTags();
        List<User> users = userService.findAll();

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedTags", filterTags);
        model.addAttribute("authors", authors);
        model.addAttribute("selectedOption", selectedOption);
        model.addAttribute("field", searchField);
        model.addAttribute("posts", posts);
        model.addAttribute("tags", tags);
        model.addAttribute("users", users);
        return "allPosts";
    }

    @GetMapping("/post/{postId}")
    public String post(@PathVariable("postId") int postId, Model theModel) {
        Post post = postService.findById(postId);
        theModel.addAttribute("post", post);
        theModel.addAttribute("Comment", new Comment());
        String tags = new String();
        List<Tag> tagList = post.getTags();
        for (int i = 0; i < tagList.size(); i++) {
            if (i == tagList.size() - 1) {
                tags += tagList.get(i);
            } else {
                tags += tagList.get(i) + ",";
            }
        }
        theModel.addAttribute(("tags"), tags);
        return "post";
    }


    @GetMapping("/update/{postId}")
    public String update(@PathVariable("postId") int postId, Model theModel) {
        Post post = postService.findById(postId);
        String tagString = new String();
        List<Tag> tagList = post.getTags();
        for (int i = 0; i < tagList.size(); i++) {
            if (i == tagList.size() - 1) {
                tagString += tagList.get(i);
            } else {
                tagString += tagList.get(i) + ",";
            }
        }
        theModel.addAttribute("Post", post);
        theModel.addAttribute("tagString", tagString);
        return "newpost";
    }

    @GetMapping("/delete/{postId}")
    public String delete(@PathVariable("postId") int theId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean userAuthorized = postService.isUserAuthorized(userDetails, theId);
        if (userAuthorized) {
            postService.deleteById(theId);
            return "redirect:/allposts";
        }
        return "accessDenied";
    }
}