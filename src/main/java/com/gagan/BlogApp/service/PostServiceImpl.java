package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.PostRepository;
import com.gagan.BlogApp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository thepostRepository) {
        postRepository = thepostRepository;
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
    public Post findById(int id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = null;

        if (result.isPresent()) {
            post = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + id);
        }
        return post;
    }
}
