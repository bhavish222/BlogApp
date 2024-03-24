package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.CommentRepository;
import com.gagan.BlogApp.dao.PostRepository;
import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private PostService postService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository ,PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository =postRepository;
    }

    @Override
    public void save(Comment comment, int postId) {
        comment.setPost(postRepository.findById(postId));
        comment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);
    }
    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
    @Override
    public Comment findById(int id) {
        Optional<Comment> result = commentRepository.findById(id);
        Comment comment = null;

        if (result.isPresent()) {
            comment = result.get();
        } else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + id);
        }
        return comment;
    }

    @Override
    public void deleteById(int id) {
        commentRepository.deleteById(id);

    }
}
