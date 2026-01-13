package com.dlut.forumx.user.entity;

import java.util.Date;

import lombok.Data;

/**
 * Corresponds to the `role_permissions` table (many-to-many relationship).
 */
@Data
public class RolePermission {
	/**
	 * Relationship record ID
	 */
	private Long id;

	/**
	 * Role ID (foreign key)
	 */
	private Integer roleId;

	/**
	 * Permission ID (foreign key)
	 */
	private Integer permissionId;

	/**
	 * Relationship creation time
	 */
	private Date createdAt;
}
