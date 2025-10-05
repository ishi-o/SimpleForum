package com.dlut.simpleforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", check = {
		@CheckConstraint(name = "user_chk_name", constraint = "name REGEXP '^[0-9a-zA-Z_\\u4e00-\\u9fff]{2,10}$'"),
		@CheckConstraint(name = "user_chk_pwd", constraint = "password REGEXP '^(?=.*[0-9].*)(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[~!@#$%&*()_+=-].*)[0-9a-zA-Z~!@#$%&*()_+=-]+$'")
}, uniqueConstraints = {
		@UniqueConstraint(name = "user_uniq_name", columnNames = { "name" })
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uid")
	private Long uid;

	@NotNull
	@Size(min = 2, max = 10, message = "{validation.user.name.size}")
	@Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fff]+$", message = "{validation.user.name.pattern}")
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@NotNull
	@Size(min = 8, max = 16, message = "{validation.user.pwd.size}")
	@Pattern(regexp = "^(?=.*[0-9].*)(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[~!@#$%&*()_+=-].*)[0-9a-zA-Z~!@#$%&*()_+=-]+$", message = "{validation.user.pwd.pattern}")
	@Column(name = "password", length = 50, nullable = false)
	private String password;

	@NotNull
	@Enumerated
	@Column(name = "role", nullable = false)
	private UserRole role;

	@NotNull
	@Enumerated
	@Column(name = "status", nullable = false)
	private UserStatus status;

	@NotNull
	@PastOrPresent
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "moderator")
	private final List<Board> boards = new ArrayList<>();

	@OneToMany(mappedBy = "author")
	private final List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "author")
	private final List<Comment> comments = new ArrayList<>();

	public User() {
	}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
		this.role = UserRole.MEMBER;
		this.createdAt = LocalDateTime.now();
		this.status = UserStatus.BLOCKED;
	}

	public enum UserRole {
		MEMBER, // 普通成员
		ADMIN // 管理员
	}

	public enum UserStatus {
		ACTIVE, // 活跃
		BLOCKED // 被封禁
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getPassword() {
		return password;
	}

	public UserRole getRole() {
		return role;
	}

	public UserStatus getStatus() {
		return status;
	}

	public Long getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public List<Board> getBoards() {
		return boards;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void changePassword(String oldVal, String newVal) {
		if (password.equals(oldVal)) {
			password = newVal;
		} else {
			throw new IllegalArgumentException("密码不正确");
		}
	}

	public void changeRole(UserRole newVal, User editor) {
		if (editor.getRole().equals(UserRole.ADMIN)) {
			role = newVal;
		} else {
			throw new IllegalArgumentException("编辑者权限不足");
		}
	}
}