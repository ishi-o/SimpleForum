package com.dlut.forumx.user.model.entity;

import java.util.Date;

import lombok.Data;

/**
 * Corresponds to the `permissions` table.
 * Note: permission_type as Integer, api_method as String.
 */
@Data
public class Permission {
	/**
	 * Permission ID
	 */
	private Integer permissionId;

	/**
	 * Permission code (unique identifier, e.g., "user:read")
	 */
	private String permissionCode;

	/**
	 * Permission display name
	 */
	private String permissionName;

	/**
	 * Permission type: 1-DATA, 2-FUNCTION, 3-API
	 */
	private Integer permissionType;

	/**
	 * Parent permission ID (for hierarchical permissions)
	 */
	private Integer parentId;

	/**
	 * Record creation time
	 */
	private Date createdAt;

	/**
	 * HTTP method: GET, POST, PUT, DELETE, *
	 */
	private String apiMethod;

	/**
	 * Request URL path (e.g., "/api/v1/users")
	 */
	private String apiPath;
}
