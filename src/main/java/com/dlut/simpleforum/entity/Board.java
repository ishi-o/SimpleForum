package com.dlut.simpleforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dlut.simpleforum.entity.User.UserRole;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "boards", uniqueConstraints = {
		@UniqueConstraint(name = "board_uniq_name", columnNames = { "name" })
}, check = {
		@CheckConstraint(name = "board_chk_name", constraint = "name REGEXP '^[0-9a-zA-Z\\u4e00-\\u9fff]{2,16}$'"),
		@CheckConstraint(name = "board_chk_descrip", constraint = "LENGTH(description) <= 40")
})
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bid")
	private Long bid;

	@NotNull
	@Size(min = 2, max = 16, message = "validation.board.name.size")
	@Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fff]+$", message = "{validation.board.name.pattern}")
	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@NotNull
	@Size(max = 40, message = "{validation.board.description.size}")
	@Column(name = "description", length = 50)
	private String description;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "moderator_id", referencedColumnName = "uid")
	private User moderator;

	@NotNull
	@PastOrPresent
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "board")
	private List<Post> posts = new ArrayList<>();

	public Board() {
	}

	public Board(String name, String despcription, User moderator) {
		this.name = name;
		this.description = despcription;
		this.moderator = moderator;
		this.createdAt = LocalDateTime.now();
	}

	public Long getBid() {
		return bid;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getDescription() {
		return description;
	}

	public User getModerator() {
		return moderator;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void changeModerator(User newModerator, User editor) {
		if (editor.getRole().equals(UserRole.ADMIN) || editor.getUid().equals(moderator.getUid())) {
			moderator = newModerator;
		} else {
			throw new IllegalArgumentException("编辑者权限不足");
		}
	}
}