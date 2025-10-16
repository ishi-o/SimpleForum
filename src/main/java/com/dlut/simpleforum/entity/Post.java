package com.dlut.simpleforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts", check = {
		@CheckConstraint(name = "post_chk_title", constraint = "title REGEXP '^[0-9a-zA-Z\u4e00-\u9fff]{2,10}$'"),
		@CheckConstraint(name = "post_chk_content", constraint = "LENGTH(content) <= 4096")
})
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pid")
	private Long pid;

	@Column(name = "title")
	private String title;

	@Lob
	@Column(name = "content")
	private String content;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "pinned")
	private Boolean isPinned;

	@Column(name = "likes")
	private Integer likes;

	@Column(name = "dislikes")
	private Integer dislikes;

	@JoinColumn(name = "author_id", referencedColumnName = "uid", updatable = false)
	@ManyToOne(optional = false)
	private User author;

	@JoinColumn(name = "board_id", referencedColumnName = "bid", updatable = false)
	@ManyToOne(optional = false)
	private Board board;

	@OneToMany(mappedBy = "cid", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MainComment> comments = new ArrayList<>();

	public Post() {
	}

	public Post(String title, String content, User author, Board board) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.board = board;
		this.createdAt = LocalDateTime.now();
		this.likes = this.dislikes = 0;
		this.isPinned = false;
	}

	public User getAuthor() {
		return author;
	}

	public Board getBoard() {
		return board;
	}

	public List<MainComment> getComments() {
		return comments;
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

	public Boolean getIsPinned() {
		return isPinned;
	}

	public Integer getLikes() {
		return likes;
	}

	public Long getPid() {
		return pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}

	public void addLikes() {
		++likes;
	}

	public void decreseLikes() {
		--likes;
	}

	public void addDislikes() {
		++dislikes;
	}

	public void decreseDislikes() {
		--dislikes;
	}

	public void trigglePin() {
		isPinned = !isPinned;
	}
}