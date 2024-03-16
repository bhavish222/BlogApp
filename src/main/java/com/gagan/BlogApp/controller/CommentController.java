package com.gagan.BlogApp.controller;


import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/addComment/{postId}")
    public String addComment(@ModelAttribute("comment") Comment comment, @PathVariable("postId") int postId) {
        //System.out.println(comment.getCommentText().getClass());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.save(comment, postId);

        return "redirect:/blog/post/" + postId;
    }

    @GetMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int theId) {
        commentService.deleteById(theId);
        return "redirect:/blog/post/" + theId;

    }
}
