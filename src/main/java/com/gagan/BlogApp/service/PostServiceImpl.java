package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.PostRepository;
import com.gagan.BlogApp.dao.TagRepository;
import com.gagan.BlogApp.dao.UserRepository;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
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
    public void savePostFromRest(Post post){
        postRepository.save(post);
    }
    @Override
    public void save(Post post, String tagString, Authentication authentication) {

        List<String> tagInPost = Arrays.asList(tagString.split(","));
        List<Tag> tagsInDb = tagRepository.findAll();
        Set<String> tagNamesInDb = new HashSet<>();
        for(Tag tag : tagsInDb)
        {
            tagNamesInDb.add(tag.getName());
        }
        post.setTags(null);
        for(String tagName : tagInPost)
        {
            if (!tagNamesInDb.contains(tagName))
            {
                Tag tag = new Tag(tagName);
                post.addtag(tag);
            }
            else {
                Tag newTag = tagRepository.findTagByName(tagName);
                post.addtag(newTag);
            }
        }
        String content = post.getContent();
        String excerpt = content.length() > 30 ? content.substring(0, 30) : content;
        post.setExcerpt(excerpt);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByName(username);
        post.setAuthor(user);
        if (post.getId() == 0) {

            post.setIsPublished(true);
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            //postRepository.save(post);
        } else {
            // Post existingPost = postRepository.findById(post.getId());
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        }
        postRepository.save(post);

    }

    @Override
    public List<Post> findAll(Integer pageNumber,Integer pageSize) {
        List<Post> posts = postRepository.findAll();
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
    public Post findById(int id) { return postRepository.findById(id); }

    @Override
    public List<Post> searchPosts(String searchText) {
        if(searchText==null || searchText.isEmpty())
        {
            return postRepository.findAll();
        }
        Set<Post> searchPosts = new HashSet<>();
        searchPosts.addAll(postRepository.findByTitleContainingOrContentContaining(searchText, searchText));
        //find user
        User user = userRepository.findByName(searchText);
        searchPosts.addAll(postRepository.findByAuthor(user));
        //find tags
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tagRepository.findTagByName(searchText));
        searchPosts.addAll(postRepository.findByTags(tags));

        return new ArrayList<>(searchPosts);
    }

    @Override
    public List<Post> filter(List<String> authors, List<String> filterTags, String startDate, String endDate) {
        Set<Post> filteredPosts = new HashSet<>();
        if (authors==null && filterTags==null && startDate==null && endDate==null) {
            return null;
        }
        if (authors != null) {
            for (String authorName : authors) {
                User user = userRepository.findByName(authorName);
                if (user != null) {
                    filteredPosts.addAll(postRepository.findByAuthor(user));
                }
            }
        }
        if (filterTags != null && !filterTags.isEmpty()) {
            for (String tagName : filterTags) {
                Tag tag = tagRepository.findTagByName(tagName);
                System.out.println(tag);
                if (tag != null) {
                    filteredPosts.addAll(postRepository.findByTags(Collections.singletonList(tag)));
                }
            }
        }
        if (startDate != null && endDate != null) {
            try {
                Timestamp startDateTime = Timestamp.valueOf(startDate + " 00:00:00");
                Timestamp endDateTime = Timestamp.valueOf(endDate + " 23:59:59");
                filteredPosts.addAll(postRepository.findPostByCreatedAtBetween(startDateTime, endDateTime));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(filteredPosts);
    }
    public List<Post> sortPosts(List<Post> posts, String selectedOption, Pageable pageable) {
        Page<Post> sortedPosts;
        if (selectedOption.equals("asc")) {
            sortedPosts = postRepository.findPostsByOrderByCreatedAtAsc(posts, pageable);
            //System.out.println("Posts coming in sort function " + posts);
        } else {
            sortedPosts = postRepository.findPostsByOrderByCreatedAtDesc(posts, pageable);
        }
        //System.out.println("Sorted posts: " + sortedPosts.getContent());
        return sortedPosts.getContent();
    }


    @Override
    public List<Post> findposts(String searchField, String selectedOption, List<String> authors, List<String> filterTags, String startDate, String endDate, Pageable pageable) {
        List<Post> searchedPosts = searchPosts(searchField);
        //System.out.println("Posts coming in search function " + searchedPosts);
        List<Post> filteredPosts = filter(authors,filterTags,startDate,endDate);
        //System.out.println("Posts coming in filtered function " + filteredPosts);
        List<Post> commonPosts = new ArrayList<>();
        if (filteredPosts == null || filteredPosts.isEmpty()) {
            commonPosts = searchedPosts;
        } else {
            for (Post post : searchedPosts) {
                if (filteredPosts.contains(post)) {
                    commonPosts.add(post);
                }
            }
        }
        //System.out.println("commmon posts " + commonPosts);
        return sortPosts(commonPosts,selectedOption,pageable);
    }

    @Override
    public void deleteById ( int id){
        postRepository.deleteById(id);
    }

    @Override
    public boolean isUserAuthorized(UserDetails userDetails, Integer postId){
        Post post = findById(postId);
        boolean isAuthorized = false;
        if( userDetails==null){
            return false;
        }
        else if(userDetails.getAuthorities().toString().contains("ROLE_ADMIN")){
            isAuthorized = true;
        } else if (userDetails.getUsername().equals(post.getAuthor().toString())) {
            isAuthorized = true;
        }

        return isAuthorized;
    }
}
