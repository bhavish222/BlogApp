package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.PostRepository;
import com.gagan.BlogApp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
