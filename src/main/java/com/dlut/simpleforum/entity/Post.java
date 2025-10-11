package com.dlut.simpleforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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

	@NotNull
	@Size(min = 2, max = 10, message = "{validation.post.title.size}")
	@Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fff]+$", message = "{validation.post.title.pattern}")
	@Column(name = "title")
	private String title;

	@NotNull
	@Size(max = 4096, message = "{validation.post.content.size}")
	@Lob
	@Column(name = "content")
	private String content;

	@NotNull
	@PastOrPresent
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@NotNull
	@Column(name = "pinned")
	private Boolean isPinned;

	@NotNull
	@PositiveOrZero
	@Column(name = "likes")
	private Integer likes;

	@NotNull
	@PositiveOrZero
	@Column(name = "dislikes")
	private Integer dislikes;

	@NotNull
	@JoinColumn(name = "author_id", referencedColumnName = "uid", updatable = false)
	@ManyToOne(optional = false)
	private User author;

	@NotNull
	@JoinColumn(name = "board_id", referencedColumnName = "bid", updatable = false)
	@ManyToOne(optional = false)
	private Board board;

	@OneToMany(mappedBy = "cid")
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
}