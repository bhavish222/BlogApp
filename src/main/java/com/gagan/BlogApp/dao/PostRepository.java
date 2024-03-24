package com.gagan.BlogApp.dao;

import com.gagan.BlogApp.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    void deletePostById(int id);
    public List<Post> findAllByOrderByPublishedAtDesc();
    public List<Post> findAllByOrderByTitle();
    List<Post> findByTitleContainingOrContentContaining(String title, String content);
    List<Post> findByAuthor(User author);
    List<Post> findByTags(List<Tag> tags);
    List<Post> findPostByCreatedAt(Timestamp date);
    List<Post> findPostByCreatedAtBetween(Timestamp start, Timestamp end);
    Post findById(int id);
    @Query("SELECT p FROM Post p WHERE p.id IN (SELECT post.id FROM Post post WHERE post IN :posts) ORDER BY p.createdAt ASC")
    Page<Post> findPostsByOrderByCreatedAtAsc(@Param("posts") List<Post> posts,Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p IN :posts ORDER BY p.createdAt DESC")
    Page<Post> findPostsByOrderByCreatedAtDesc(@Param(value = "posts") List<Post> posts,Pageable pageable);

//    List<Post> findByIdInOrderByCreatedAtAsc(List<Integer> postIds);
//    List<Post> findByIdInOrderByCreatedAtDesc(List<Integer> postIds);
}
