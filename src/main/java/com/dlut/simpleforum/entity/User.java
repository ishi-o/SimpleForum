package com.dlut.simpleforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dlut.simpleforum.common.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users", check = {
		@CheckConstraint(name = "user_chk_name", constraint = "name REGEXP '^[0-9a-zA-Z_\u4e00-\u9fff]{2,10}$'"),
}, uniqueConstraints = {
		@UniqueConstraint(name = "user_uniq_name", columnNames = { "name" })
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uid")
	private Long uid;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Enumerated
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@JsonIgnore
	@OneToMany(mappedBy = "moderator")
	private final List<Board> boards = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "author")
	private final List<Post> posts = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "author")
	private final List<Comment> comments = new ArrayList<>();

	public User() {
	}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
		this.role = UserRole.USER;
		this.createdAt = LocalDateTime.now();
	}

	public void setName(String name) {
		this.name = name;
	}
}