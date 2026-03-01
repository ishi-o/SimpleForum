package com.dlut.forumx.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.content.entity.Comment;

@Mapper
public interface CommentMapper {
	int insert(Comment comment);

	int deleteById(@Param("cid") Long cid);

	int update(Comment comment);

	Comment selectById(@Param("cid") Long cid);

	List<Comment> selectByPostId(@Param("postId") Long postId,
			@Param("status") Integer status,
			@Param("offset") int offset,
			@Param("limit") int limit);

	List<Comment> selectByParentId(@Param("parentId") Long parentId,
			@Param("status") Integer status);

	List<Comment> selectByAuthorId(@Param("authorId") String authorId,
			@Param("offset") int offset,
			@Param("limit") int limit);

	long countByPostId(@Param("postId") Long postId, @Param("status") Integer status);

	int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
