package com.dlut.simpleforum.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dlut.simpleforum.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByBoardBid(Long bid, Pageable pageable);

	Optional<Post> findByBoardBidAndPid(Long bid, Long pid);

	@Query("""
			SELECT p
			FROM Post p
			WHERE p.board.bid = :bid AND
				( p.title LIKE %:pattern% OR
				p.content LIKE %:pattern% )
			""")
	Page<Post> findByTitleOrContentContaining(Long bid, String pattern, Pageable pageable);
}
