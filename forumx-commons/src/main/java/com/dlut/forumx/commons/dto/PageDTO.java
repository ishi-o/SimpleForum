package com.dlut.forumx.commons.dto;

import lombok.Data;
import java.util.List;

/**
 * 通用分页响应DTO
 * 纯 POJO，不依赖任何框架
 */
@Data
public class PageDTO<T> {

	/**
	 * 数据列表
	 */
	private List<T> list;

	/**
	 * 当前页码（从1开始）
	 */
	private Integer pageNum;

	/**
	 * 每页大小
	 */
	private Integer pageSize;

	/**
	 * 总记录数
	 */
	private Long total;

	/**
	 * 总页数
	 */
	private Integer pages;

	/**
	 * 是否有下一页
	 */
	private Boolean hasNext;

	/**
	 * 是否有上一页
	 */
	private Boolean hasPrevious;
}
