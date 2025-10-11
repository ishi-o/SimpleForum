package com.dlut.simpleforum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dlut.simpleforum.entity.Comment;
import com.dlut.simpleforum.entity.MainComment;
import com.dlut.simpleforum.entity.SubComment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("""
				SELECT mc
				FROM MainComment mc
				WHERE mc.post.pid = :pid
					AND mc.post.board.bid = :bid
			""")
	Page<MainComment> findMainCommentsByPostPidAndPostBoardBid(
			@Param("pid") Long pid,
			@Param("bid") Long bid,
			Pageable pageable);

	@Query("""
			SELECT sc
			FROM SubComment sc
			WHERE sc.parent.post.pid = :pid
				AND sc.parent.post.board.bid = :bid
				AND sc.parent.cid = :cid
				""")
	Page<SubComment> findSubCommentsAllByPostPidAndPostBoardBidAndParentCid(
			@Param("pid") Long pid,
			@Param("bid") Long bid,
			@Param("cid") Long cid,
			Pageable pageable);
}
