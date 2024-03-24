package com.gagan.BlogApp.service;

import com.gagan.BlogApp.dao.TagRepository;
import com.gagan.BlogApp.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags;
    }

    @Override
    public Tag findTagByName(String name) {

        return tagRepository.findTagByName(name);
    }
}
