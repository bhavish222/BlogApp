package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.*;
import com.gagan.BlogApp.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllPostSortedByDate() {
        return postRepository.findAllByOrderByPublishedAtDesc();
    }


    @Override
    public List<Post> findAllPostSortedByTitle() {
        return postRepository.findAllByOrderByTitle();
    }

    @Override
    public Post findById(int id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = null;

        if (result.isPresent()) {
            post = result.get();
        } else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + id);
        }
        return post;
    }

    @Override
    public List<Post> searchPosts(String searchText) {
        List<Post> searchPosts = new ArrayList<>();

        searchPosts.addAll(postRepository.findByTitleContainingOrContentContaining(searchText, searchText));
        //find user
        User user = userRepository.findByName(searchText);
        searchPosts.addAll(postRepository.findByAuthor(user));
        //find tags
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tagRepository.findTagsByName(searchText));
        searchPosts.addAll(postRepository.findByTags(tags));

        return searchPosts;
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);

    }

}
