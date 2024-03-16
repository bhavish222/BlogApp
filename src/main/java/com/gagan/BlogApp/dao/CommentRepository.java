package com.gagan.BlogApp.dao;

import com.gagan.BlogApp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment ,Integer> {
}
