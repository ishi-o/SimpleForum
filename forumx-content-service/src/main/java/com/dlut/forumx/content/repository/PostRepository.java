package com.dlut.forumx.content.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dlut.forumx.content.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByBoardBid(Long bid, Pageable pageable);

	Optional<Post> findByBoardBidAndPid(Long bid, Long pid);

	@Query("""
			SELECT p
			FROM Post p
			WHERE p.board.bid = :bid AND
			    ( p.title LIKE CONCAT('%', :pattern, '%') OR
			    p.content LIKE CONCAT('%', :pattern, '%') )
			""")
	Page<Post> findByTitleOrContentContaining(@Param("bid") Long bid,
			@Param("pattern") String pattern,
			Pageable pageable);
}
