package com.gagan.BlogApp.controller;


import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.service.CommentService;
import com.gagan.BlogApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class CommentController {
    private CommentService commentService;
    private PostService postService;

    @Autowired
    public CommentController(CommentService commentService,PostService postService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/addComment/{postId}")
    public String addComment(@ModelAttribute("comment") Comment comment, @PathVariable("postId") int postId) {
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.save(comment, postId);
        return "redirect:/post/" + postId;
    }


    @GetMapping("/updateComment/{commentId}")
    public String updateComment(@PathVariable("commentId") int id, Model theModel) {
        Comment comment = commentService.findById(id);
        int postId = comment.getPost().getId();
        Post post = postService.findById(postId);
        theModel.addAttribute("Comment", comment);
        theModel.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int theId ) {
        Comment comment = commentService.findById(theId);
        commentService.deleteById(theId);
        int postId = comment.getPost().getId();
        return "redirect:/post/" + postId;
    }
}