package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.entity.User;
import com.gagan.BlogApp.service.PostService;
import com.gagan.BlogApp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/blog")
public class PostController {

    private PostService postService;
    private TagService tagService;

    @Autowired
    public PostController(PostService thePostService, TagService tagService) {
        postService = thePostService;
        this.tagService = tagService;
    }

    @GetMapping("/newpost")
    public String newPost(Model theModel) {
        Post post = new Post();
        theModel.addAttribute("Post", post);
        return "newpost";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute("Post") Post post, @RequestParam("tagString")String tagString, Model model) {
        System.out.println(tagString);

        List<String> tagInPost = Arrays.asList(tagString.split(","));
        List<Tag> tagsInDb = tagService.findAllTags();
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
                Tag newTag = tagService.findTagByName(tagName);
                post.addtag(newTag);
            }
        }
        if (post.getId() == 0) {
            String content = post.getContent();
            String excerpt = content.length() > 30 ? content.substring(0, 30) : content;
            post.setExcerpt(excerpt);
            post.setIsPublished(true);
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            User user = new User(1, "Gagan", "gagan@gmail.com", "1234");
            model.addAttribute("user", user);
            post.setAuthor(user);
            postService.save(post);
        } else {
            Post existingPost = postService.findById(post.getId());
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            postService.save(post);
        }

        return "redirect:/blog/allposts";
    }

    @GetMapping("/allposts")
    public String allPosts(Model theModel) {

        // create model attribute to bind form data
        List<Post> posts = postService.findAll();
        theModel.addAttribute("posts", posts);
        return "allPosts";
    }

    @GetMapping("/post/{postId}")
    public String post(@PathVariable("postId") int postId, Model theModel) {
        Post post = postService.findById(postId);
        theModel.addAttribute("post", post);
        theModel.addAttribute("Comment",new Comment());
        return "post";
    }

    @GetMapping("/update/{postId}")
    public String update(@PathVariable("postId") int postId, Model theModel) {
        // Use the postService to find the post by its ID
        Post post = postService.findById(postId);
        String tagString = new String();
        List<Tag> tagList = post.getTags();
        for(int i=0;i<tagList.size();i++)
        {
            if(i==tagList.size()-1)
            {
                tagString+=tagList.get(i);
            }
            else {
                tagString +=tagList.get(i)+ ",";
            }
        }
        theModel.addAttribute("Post", post);
        theModel.addAttribute("tagString", tagString);
        return "newpost";
    }

    @GetMapping("/delete/{postId}")
    public String delete(@PathVariable("postId") int theId) {
        postService.deleteById(theId);
        return "redirect:/blog/allposts";

    }


}