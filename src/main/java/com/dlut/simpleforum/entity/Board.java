package com.dlut.simpleforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.util.MessageSourceUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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

@Entity
@Table(name = "boards", uniqueConstraints = {
		@UniqueConstraint(name = "board_uniq_name", columnNames = { "name" })
}, check = {
		@CheckConstraint(name = "board_chk_name", constraint = "name REGEXP '^[0-9a-zA-Z\u4e00-\u9fff]{2,16}$'"),
		@CheckConstraint(name = "board_chk_descrip", constraint = "LENGTH(description) <= 40")
})
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bid")
	private Long bid;

	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@Column(name = "description", length = 50)
	private String description;

	@ManyToOne(optional = false)
	@JoinColumn(name = "moderator_id", referencedColumnName = "uid")
	private User moderator;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@JsonIgnore
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
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

	public void setModerator(User newModerator) {
		moderator = newModerator;
	}

	public void checkPermission(User editor) {
		if (!editor.getRole().equals(UserRole.ADMIN) && !editor.getUid().equals(moderator.getUid())) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.editor.no-permission", null));
		}
	}
}