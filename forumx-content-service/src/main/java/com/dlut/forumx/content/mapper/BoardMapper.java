package com.dlut.forumx.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.content.entity.Board;

@Mapper
public interface BoardMapper {
	int insert(Board board);

	int deleteById(@Param("bid") Long bid);

	int update(Board board);

	Board selectById(@Param("bid") Long bid);

	Board selectByName(@Param("name") String name);

	Board selectBySlug(@Param("slug") String slug);

	List<Board> selectAll();

	List<Board> selectByModeratorId(@Param("moderatorId") String moderatorId);

	List<Board> selectPage(@Param("offset") int offset, @Param("limit") int limit);
}
