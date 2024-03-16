package com.gagan.BlogApp.dao;

import com.gagan.BlogApp.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer>{
    Tag findTagsByName(String name);


}
