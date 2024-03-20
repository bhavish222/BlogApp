package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.Post;

import java.util.List;
import java.util.Map;

public interface PostService {

    void save(Post post);

    List<Post> findAll(Integer pageNumber,Integer pageSize);

    List<Post> findAllPostSortedByDate();
    List<Post> searchPosts(String title);
    //List<Post> searchPostsByAuthorAndTag(String authorName, String tagName)

    List<Post> filter(Map<String, String> map);

    List<Post> findAllPostSortedByTitle();
    Post findById(int id);

    void deleteById(int id);

}
