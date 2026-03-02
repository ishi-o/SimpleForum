package com.dlut.forumx.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.user.entity.User;

@Mapper
public interface UserMapper {
	int insert(User user);

	int updateById(User user);

	int updateByKeycloakId(User user);

	User selectById(@Param("id") Long id);

	User selectByKeycloakId(@Param("keycloakId") String keycloakId);

	User selectByUsername(@Param("username") String username);

	User selectByEmail(@Param("email") String email);

	List<User> selectByIds(@Param("ids") List<Long> ids);

	int deleteByKeycloakId(@Param("keycloakId") String keycloakId);

	int updateLastLogin(@Param("userId") Long userId, @Param("ip") String ip);
}
