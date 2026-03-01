package com.dlut.forumx.common.content.dto.result;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
	private List<T> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private Boolean first;
	private Boolean last;

	public static <T> PageResult<T> from(PageInfo<T> pageInfo) {
		return new PageResult<>(
				pageInfo.getList(),
				pageInfo.getPageNum(),
				pageInfo.getPageSize(),
				pageInfo.getTotal(),
				pageInfo.getPages(),
				pageInfo.isIsFirstPage(),
				pageInfo.isIsLastPage());
	}

	public static <T, S> PageResult<T> from(PageResult<S> pageResult, Function<S, T> mapper) {
		return new PageResult<>(
				pageResult.content.stream().map(mapper).collect(Collectors.toList()),
				pageResult.pageNumber,
				pageResult.pageSize,
				pageResult.totalElements,
				pageResult.totalPages,
				pageResult.first,
				pageResult.last);
	}
}
