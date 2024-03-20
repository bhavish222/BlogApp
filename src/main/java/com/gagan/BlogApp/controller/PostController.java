package com.gagan.BlogApp.controller;

import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.entity.User;
import com.gagan.BlogApp.service.PostService;
import com.gagan.BlogApp.service.TagService;
import com.gagan.BlogApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.*;

@Controller
//@RequestMapping("/blog")
public class PostController {

    private PostService postService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
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

            User user = new User(2, "Rahul", "middha@gmail.com", "2001");
            model.addAttribute("user", user);
            post.setAuthor(user);
            postService.save(post);
        } else {
            Post existingPost = postService.findById(post.getId());
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            postService.save(post);
        }

        return "redirect:/allposts";
    }
    @GetMapping("/allposts")
    public String allPosts(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                           @RequestParam(value = "pageSize",defaultValue = "3",required = false)Integer pageSize,
                           Model model) {

        List<Post> posts = postService.findAll(pageNumber,pageSize);
        model.addAttribute("posts",posts);
        model.addAttribute("pageNumber",pageNumber);
        return prepareModelAndReturnView(posts, model);
    }

    @GetMapping("/post/{postId}")
    public String post(@PathVariable("postId") int postId, Model theModel) {
        Post post = postService.findById(postId);
        theModel.addAttribute("post", post);
        theModel.addAttribute("Comment",new Comment());
        String tags = new String();
        List<Tag> tagList = post.getTags();
        for(int i=0;i<tagList.size();i++)
        {
            if(i==tagList.size()-1)
            {
                tags+=tagList.get(i);
            }
            else {
                tags +=tagList.get(i)+ ",";
            }
        }
        theModel.addAttribute(("tags"),tags);
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
        return "redirect:/allposts";

    }

    @PostMapping("/sort")
    public String sortPosts(@ModelAttribute("selectedOption") String selectedOption, Model model){
        List<Post> posts = new ArrayList<>();
        if(selectedOption.equals("date")) {
            posts.addAll(postService.findAllPostSortedByDate());
            model.addAttribute("posts", posts);
        }
        else if(selectedOption.equals("title")){
            posts.addAll(postService.findAllPostSortedByTitle());
            model.addAttribute("posts", posts);
        }
//        else{
//            posts.addAll(postService.findAll());
//            model.addAttribute("posts",posts);
//        }

        return prepareModelAndReturnView(posts, model);
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("field") String searchField , Model model) {
        // Now you can use the searchField variable to access the value sent from the form
        System.out.println("Search Field: " + searchField);

        List<Post> posts = postService.searchPosts(searchField);
        return prepareModelAndReturnView(posts, model);
    }
    @GetMapping("/filter")
    public String filter( @RequestParam Map<String, String> op, Model model) {

        List<Post> posts = postService.filter(op);
        return prepareModelAndReturnView(posts, model);
    }

    private String prepareModelAndReturnView(List<Post> posts, Model model) {
        List<Tag> tags = tagService.findAllTags();
        List<User> users = userService.findAll();

        model.addAttribute("posts", posts);
        model.addAttribute("tags", tags);
        model.addAttribute("users", users);
        return "allPosts";
    }
}