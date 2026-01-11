package com.dlut.simpleforum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlut.simpleforum.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByName(String name);
}
