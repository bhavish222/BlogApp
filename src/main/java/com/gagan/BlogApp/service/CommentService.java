package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;

import java.util.List;

public interface CommentService {
    void save(Comment comment ,int postId);

    List<Comment> findAll();

    Comment findById(int id);

    void deleteById(int id);
}
