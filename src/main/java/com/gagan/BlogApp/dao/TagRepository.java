package com.gagan.BlogApp.dao;

import com.gagan.BlogApp.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer>{
    Tag findTagByName(String name);
    List<Tag> findTagsByNameIn(List<String> name);

}
