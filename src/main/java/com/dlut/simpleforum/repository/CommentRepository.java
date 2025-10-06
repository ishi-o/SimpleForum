package com.dlut.simpleforum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlut.simpleforum.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
