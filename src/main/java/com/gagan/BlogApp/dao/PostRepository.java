package com.gagan.BlogApp.dao;

import com.gagan.BlogApp.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    void deletePostById(int id);
    public List<Post> findAllByOrderByPublishedAtDesc();
    public List<Post> findAllByOrderByTitle();
    List<Post> findByTitleContainingOrContentContaining(String title, String content);
    List<Post> findByAuthor(User author);
    List<Post> findByTags(List<Tag> tags);

}
