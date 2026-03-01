package com.dlut.forumx.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.content.entity.Post;

@Mapper
public interface PostMapper {
	int insert(Post post);

	int deleteById(@Param("pid") Long pid);

	int update(Post post);

	Post selectById(@Param("pid") Long pid);

	Post selectWithDetailsById(@Param("pid") Long pid);

	List<Post> selectByAuthorId(@Param("authorId") String authorId,
			@Param("offset") int offset,
			@Param("limit") int limit);

	List<Post> selectByBoardId(@Param("boardId") Long boardId,
			@Param("status") Integer status,
			@Param("offset") int offset,
			@Param("limit") int limit);

	List<Post> selectPinnedPosts(@Param("boardId") Long boardId);

	List<Post> selectEssencePosts(@Param("boardId") Long boardId,
			@Param("offset") int offset,
			@Param("limit") int limit);

	List<Post> selectLatestPosts(@Param("boardId") Long boardId,
			@Param("status") Integer status,
			@Param("offset") int offset,
			@Param("limit") int limit);

	int incrementViewCount(@Param("pid") Long pid);

	long countByStatus(@Param("status") Integer status);

	long countByBoardId(@Param("boardId") Long boardId, @Param("status") Integer status);

	List<Post> search(@Param("keyword") String keyword,
			@Param("boardId") Long boardId,
			@Param("status") Integer status,
			@Param("offset") int offset,
			@Param("limit") int limit);
}
