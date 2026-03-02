package com.dlut.forumx.commons.dto.search;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子搜索文档
 * 用于从content-service同步到ES，以及返回给前端
 */
@Data
public class PostSearchDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 帖子ID
	 */
	private Long id;

	/**
	 * 作者ID
	 */
	private Long userId;

	/**
	 * 作者用户名
	 */
	private String username;

	/**
	 * 作者昵称
	 */
	private String nickname;

	/**
	 * 作者头像
	 */
	private String avatar;

	/**
	 * 帖子标题
	 */
	private String title;

	/**
	 * 帖子内容
	 */
	private String content;

	/**
	 * 图片列表
	 */
	private List<String> images;

	/**
	 * 标签列表
	 */
	private List<String> tags;

	/**
	 * 点赞数
	 */
	private Integer likeCount;

	/**
	 * 评论数
	 */
	private Integer commentCount;

	/**
	 * 收藏数
	 */
	private Integer favoriteCount;

	/**
	 * 浏览数
	 */
	private Integer viewCount;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 状态 0-删除 1-正常
	 */
	private Integer status;
}
