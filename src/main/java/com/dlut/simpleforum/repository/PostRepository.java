package com.dlut.simpleforum.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dlut.simpleforum.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByBoardBid(Long bid, Pageable pageable);

	Optional<Post> findByBoardBidAndPid(Long bid, Long pid);
}
