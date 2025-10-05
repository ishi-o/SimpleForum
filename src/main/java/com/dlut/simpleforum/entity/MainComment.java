package com.dlut.simpleforum.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "main_comments")
@PrimaryKeyJoinColumn(name = "cid")
public class MainComment extends Comment {
	@NotNull
	@JoinColumn(name = "post_id", referencedColumnName = "pid")
	@ManyToOne(optional = false)
	private Post post;

	@OneToMany(mappedBy = "parent")
	private final List<SubComment> replies = new ArrayList<>();

	public MainComment(String content, User author, Post post) {
		super(content, author);
		this.post = post;
	}

	public Post getPost() {
		return post;
	}

	@Override
	public List<SubComment> getReplies() {
		return replies;
	}
}
