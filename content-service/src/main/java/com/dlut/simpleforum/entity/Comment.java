package com.dlut.simpleforum.entity;

import java.time.LocalDateTime; // 导入时间处理类
import java.util.List;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "comments", check = {
		@CheckConstraint(name = "comment_chk_content", constraint = "LENGTH(content) BETWEEN 1 AND 128"),
		@CheckConstraint(name = "comment_chk_positive", constraint = "likes >= 0 AND dislikes >= 0")
})
public abstract class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cid")
	private Long cid;

	@Column(name = "content")
	private String content;

	@JoinColumn(name = "author_id", referencedColumnName = "uid")
	@ManyToOne(optional = false)
	private User author;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "likes")
	private Integer likes;

	@Column(name = "dislikes")
	private Integer dislikes;

	public Comment() {
	}

	public Comment(String content, User author) {
		this.content = content;
		this.author = author;
		this.likes = this.dislikes = 0;
		this.createdAt = LocalDateTime.now();
	}

	public User getAuthor() {
		return author;
	}

	public Long getCid() {
		return cid;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public Integer getLikes() {
		return likes;
	}

	public abstract List<? extends Comment> getReplies();

	public void addLikes() {
		++likes;
	}

	public void decreaseLikes() {
		--likes;
	}

	public void addDislikes() {
		++dislikes;
	}

	public void decreaseDislikes() {
		--dislikes;
	}
}