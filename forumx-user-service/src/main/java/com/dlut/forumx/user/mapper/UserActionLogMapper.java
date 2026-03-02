package com.dlut.forumx.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.user.entity.UserActionLog;

@Mapper
public interface UserActionLogMapper {
	int insert(UserActionLog log);

	List<UserActionLog> selectByUser(@Param("userId") Long userId,
			@Param("offset") int offset,
			@Param("limit") int limit);

	List<UserActionLog> selectByAction(@Param("actionType") String actionType,
			@Param("offset") int offset,
			@Param("limit") int limit);
}
