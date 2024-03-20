package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.PostRepository;
import com.gagan.BlogApp.dao.TagRepository;
import com.gagan.BlogApp.dao.UserRepository;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;


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
    public List<Post> findAll(Integer pageNumber,Integer pageSize) {

        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Post> pagePost = postRepository.findAll(p);
        List<Post> posts = pagePost.getContent();
        return posts;
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
    public List<Post> filter(Map<String, String> map) {
        Set<Post> filteredPosts = new HashSet<>();
        System.out.println(map);
        boolean hasFilter = false;
        for (String value : map.values()) {
            if (value != null && !value.isEmpty()) {
                hasFilter = true;
                break;
            }
        }

        if (!hasFilter) {
            return postRepository.findAll();
        }
        //List<Post> x = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("author")) {
                User user = userRepository.findByName(entry.getValue());
                filteredPosts.addAll(postRepository.findByAuthor(user));
            }
            if (entry.getKey().equals("tag")) {
                List<Tag> tags = new ArrayList<Tag>();
                tags.add(tagRepository.findTagsByName(entry.getValue()));
                filteredPosts.addAll(postRepository.findByTags(tags));
            }
            if (entry.getKey().equals("startDate") || entry.getKey().equals("endDate")) {
                try {
                    String startDateString = map.get("startDate");
                    String endDateString = map.get("endDate");
                    Timestamp startDate = Timestamp.valueOf(startDateString + " 00:00:00");
                    Timestamp endDate = Timestamp.valueOf(endDateString + " 00:00:00");
                    filteredPosts.addAll(postRepository.findPostByCreatedAtBetween(startDate, endDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<>(filteredPosts);
    }

    @Override
    public void deleteById ( int id){
        postRepository.deleteById(id);
    }
}
