package com.dlut.forumx.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.user.model.entity.Role;
import com.dlut.forumx.user.model.entity.User;

@Mapper
public interface UserMapper {
	public User selectByUsername(@Param("username") String username);

	public User selectByPhone(@Param("phone") String phone);

	public User selectByEmail(@Param("email") String email);

	public List<Role> selectAuthoritiesById(@Param("id") Long userId);
}
