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
@Table(name = "sub_comments")
@PrimaryKeyJoinColumn(name = "cid")
public class SubComment extends Comment {
	@JoinColumn(name = "parent_cid", referencedColumnName = "cid", foreignKey = @ForeignKey(name = "fk_subcmt_maincmt", foreignKeyDefinition = "FOREIGN KEY (parent_cid) REFERENCES main_comments(cid) ON DELETE CASCADE"))
	@ManyToOne(optional = false)
	private MainComment parent;

	@JoinColumn(name = "target_cid", referencedColumnName = "cid", foreignKey = @ForeignKey(name = "fk_subcmt_subcmt", foreignKeyDefinition = "FOREIGN KEY (target_cid) REFERENCES sub_comments(cid) ON DELETE SET NULL"))
	@ManyToOne
	private SubComment target;

	@JsonIgnore
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
