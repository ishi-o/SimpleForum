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
import lombok.Getter;

@Getter
@Entity
@Table(name = "users", check = {
		@CheckConstraint(name = "user_chk_name", constraint = "name REGEXP '^[0-9a-zA-Z_\u4e00-\u9fff]{2,10}$'"),
		@CheckConstraint(name = "user_chk_pwd", constraint = "password REGEXP '^(?=.*[0-9].*)(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[~!@#$%&*()_+=-].*)[0-9a-zA-Z~!@#$%&*()_+=-]{8,16}$'")
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

	@Column(name = "password", length = 50, nullable = false)
	private String password;

	@Enumerated
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Enumerated
	@Column(name = "status", nullable = false)
	private UserStatus status;

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
		ADMIN, // 管理员
		GUEST // 游客
	}

	public enum UserStatus {
		ACTIVE, // 活跃
		OFFLINE, // 下线
		BLOCKED // 被封禁
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