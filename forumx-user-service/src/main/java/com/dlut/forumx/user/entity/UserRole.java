package com.dlut.forumx.user.entity;

import java.util.Date;

import lombok.Data;

/**
 * Corresponds to the `user_roles` table (many-to-many relationship).
 */
@Data
public class UserRole {
	/**
	 * Relationship record ID
	 */
	private Long id;

	/**
	 * User ID (foreign key)
	 */
	private Long userId;

	/**
	 * Role ID (foreign key)
	 */
	private Integer roleId;

	/**
	 * Relationship creation time
	 */
	private Date createdAt;
}
