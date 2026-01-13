package com.dlut.forumx.user.entity;

import java.util.Date;

import lombok.Data;

/**
 * Corresponds to the `users` table.
 */
@Data
public class User {
	/**
	 * User ID
	 */
	private Long userId;

	/**
	 * Username
	 */
	private String username;

	/**
	 * Email address
	 */
	private String email;

	/**
	 * Phone number
	 */
	private String phone;

	/**
	 * Encrypted password
	 */
	private String password;

	/**
	 * Nickname
	 */
	private String nickname;

	/**
	 * Avatar URL
	 */
	private String avatarUrl;

	/**
	 * Status: 0-disabled, 1-active, 2-locked
	 */
	private Integer status;

	/**
	 * Last login time
	 */
	private Date lastLoginTime;

	/**
	 * Record creation time
	 */
	private Date createdAt;

	/**
	 * Record last update time
	 */
	private Date updatedAt;

	/**
	 * Soft delete timestamp (NULL means not deleted)
	 */
	private Date deletedAt;
}
