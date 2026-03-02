package com.dlut.forumx.commons.dto.search;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 搜索请求参数
 */
@Data
public class SearchRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 搜索关键词
	 */
	private String keyword;

	/**
	 * 当前页码，从1开始
	 */
	private Integer pageNum = 1;

	/**
	 * 每页大小
	 */
	private Integer pageSize = 10;

	/**
	 * 排序字段
	 */
	private String sortBy = "createTime";

	/**
	 * 排序方向 asc/desc
	 */
	private String sortOrder = "desc";

	/**
	 * 标签过滤
	 */
	private List<String> tags;

	/**
	 * 作者ID过滤
	 */
	private Long userId;

	/**
	 * 搜索类型 post/user
	 */
	private String type = "post";
}
