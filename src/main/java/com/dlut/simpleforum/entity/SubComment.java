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
@Table(name = "sub_comments")
@PrimaryKeyJoinColumn(name = "cid")
public class SubComment extends Comment {
	@NotNull
	@JoinColumn(name = "parent_cid", referencedColumnName = "cid")
	@ManyToOne(optional = false)
	private MainComment parent;

	@JoinColumn(name = "target_cid", referencedColumnName = "cid")
	@ManyToOne
	private SubComment target;

	@OneToMany(mappedBy = "target")
	private final List<SubComment> replies = new ArrayList<>();

	public SubComment(String content, User author, MainComment parent, SubComment target) {
		super(content, author);
		this.parent = parent;
		this.target = target;
	}

	public SubComment() {
	}

	public MainComment getParent() {
		return parent;
	}

	public SubComment getTarget() {
		return target;
	}

	@Override
	public List<SubComment> getReplies() {
		return replies;
	}
}
