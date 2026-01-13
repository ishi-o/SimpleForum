package com.dlut.forumx.user.entity;

import java.util.Date;

import lombok.Data;

/**
 * Corresponds to the `roles` table.
 */
@Data
public class Role {
	/**
	 * Role ID
	 */
	private Integer roleId;

	/**
	 * Role code (unique identifier)
	 */
	private String roleCode;

	/**
	 * Role display name
	 */
	private String roleName;

	/**
	 * Role description
	 */
	private String description;

	/**
	 * Record creation time
	 */
	private Date createdAt;
}
