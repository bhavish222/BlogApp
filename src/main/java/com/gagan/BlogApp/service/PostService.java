package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.Post;
import java.util.List;

public interface PostService {

    void save(Post post);

    List<Post> findAll();

    List<Post> findAllPostSortedByDate();

    List<Post> searchPosts(String title);

    List<Post> findAllPostSortedByTitle();

    Post findById(int id);

    void deleteById(int id);

}
