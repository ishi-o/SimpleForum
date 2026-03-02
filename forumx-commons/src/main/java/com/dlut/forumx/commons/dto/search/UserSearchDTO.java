package com.dlut.forumx.commons.dto.search;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户搜索文档
 * 用于从user-service同步到ES，以及返回给前端
 */
@Data
public class UserSearchDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 个人简介
	 */
	private String bio;

	/**
	 * 性别 0-未知 1-男 2-女
	 */
	private Integer gender;

	/**
	 * 所在地
	 */
	private String location;

	/**
	 * 关注数
	 */
	private Integer followCount;

	/**
	 * 粉丝数
	 */
	private Integer fansCount;

	/**
	 * 发帖数
	 */
	private Integer postCount;

	/**
	 * 注册时间
	 */
	private LocalDateTime registerTime;

	/**
	 * 状态 0-禁用 1-正常
	 */
	private Integer status;
}
