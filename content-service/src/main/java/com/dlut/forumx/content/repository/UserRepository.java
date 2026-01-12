package com.dlut.forumx.content.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlut.forumx.content.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByName(String name);
}
