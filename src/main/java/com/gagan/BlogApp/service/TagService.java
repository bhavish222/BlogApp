package com.gagan.BlogApp.service;

import com.gagan.BlogApp.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAllTags();

    Tag findTagByName(String name);
}
