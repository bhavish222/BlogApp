package com.gagan.BlogApp.restcontroller;

import com.gagan.BlogApp.entity.Comment;
import com.gagan.BlogApp.entity.Post;
import com.gagan.BlogApp.entity.Tag;
import com.gagan.BlogApp.service.PostService;
import com.gagan.BlogApp.service.TagService;
import com.gagan.BlogApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostRestController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }


    @PostMapping("/newpost")
    public String newPost(@RequestBody Post post) {
        postService.savePostFromRest(post);
        return "saved ";
    }

    @GetMapping("/allposts")
    public List<Post> allPosts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "4", required = false) Integer pageSize,
                               @RequestParam(value = "field", required = false) String searchField,
                               @RequestParam(value = "selectedOption", defaultValue = "desc", required = false) String selectedOption,
                               @RequestParam(value = "authors", required = false) List<String> authors,
                               @RequestParam(value = "tags", required = false) List<String> filterTags,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "endDate", required = false) String endDate,
                               Model model) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Post> posts = postService.findposts(searchField, selectedOption, authors, filterTags, startDate, endDate, pageable);

        return posts;
    }

    @GetMapping("/post/{postId}")
    public Post post(@PathVariable("postId") int postId, Model theModel) {
        Post post = postService.findById(postId);
        theModel.addAttribute("post", post);
        theModel.addAttribute("Comment", new Comment());
        String tags = new String();
        List<Tag> tagList = post.getTags();
        for (int i = 0; i < tagList.size(); i++) {
            if (i == tagList.size() - 1) {
                tags += tagList.get(i);
            } else {
                tags += tagList.get(i) + ",";
            }
        }
        theModel.addAttribute(("tags"), tags);
        return post;
    }

    @DeleteMapping("/delete/{postId}")
    public String delete(@PathVariable("postId") int theId) {
        postService.deleteById(theId);
        return "Deleted post with id " + theId;

    }
}
