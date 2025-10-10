package com.dlut.simpleforum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlut.simpleforum.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
