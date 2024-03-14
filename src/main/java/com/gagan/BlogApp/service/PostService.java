package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.Post;

import java.util.List;

public interface PostService {

    void save(Post post);
    List<Post> findAll();

    Post findById(int id);

}
