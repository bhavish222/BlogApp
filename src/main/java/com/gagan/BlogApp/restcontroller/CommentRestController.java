package com.gagan.BlogApp.restcontroller;

import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/addComment/{postId}")
    public String addComment(@RequestBody Comment comment, @PathVariable("postId") int postId) {
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.save(comment, postId);
        return "comment added";
    }

    @GetMapping("/comment/{commentId}")
    public Comment getComment(@PathVariable("commentId") int id) {
        return commentService.findById(id);
    }

    @PutMapping("/updateComment/{commentId}")
    public Comment updateComment(@PathVariable("commentId") int id, @RequestBody Comment updatedComment) {
        Comment comment = commentService.findById(id);
        comment.setCommentText(updatedComment.getCommentText());
        comment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        int postId = comment.getPost().getId();
        commentService.save(comment, postId);
        return comment;
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int theId) {
        Comment comment = commentService.findById(theId);
        commentService.deleteById(theId);
        int postId = comment.getPost().getId();
        return "deleted comment" + theId;
    }
}
