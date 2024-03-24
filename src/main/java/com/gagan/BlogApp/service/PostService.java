package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PostService {

    void save(Post post);
    List<Post> findAll(Integer pageNumber,Integer pageSize);
    List<Post> findAllPostSortedByDate();
    //List<Post> searchPosts(String title);
    List<Post> searchPosts(String title);
    //List<Post> searchPostsByAuthorAndTag(String authorName, String tagName)
    List<Post> filter(List<String> authors, List<String> filterTags, String startDate, String endDate);
    List<Post> findposts(String searchField, String selectedOption, List<String> authors, List<String> filterTags, String startDate, String endDate, Pageable pageable);
    List<Post> findAllPostSortedByTitle();
    Post findById(int id);
    void deleteById(int id);
}
