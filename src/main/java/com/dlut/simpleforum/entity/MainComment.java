package com.dlut.simpleforum.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "main_comments")
@PrimaryKeyJoinColumn(name = "cid")
public class MainComment extends Comment {
	@JoinColumn(name = "post_id", referencedColumnName = "pid", foreignKey = @ForeignKey(name = "fk_comment_post", foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES posts(pid) ON DELETE CASCADE"))
	@ManyToOne(optional = false)
	private Post post;

	@JsonIgnore
	@OneToMany(mappedBy = "parent")
	private final List<SubComment> replies = new ArrayList<>();

	public MainComment() {
	}

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
