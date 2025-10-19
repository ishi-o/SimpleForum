package com.dlut.simpleforum.dto.result;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
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

	public static <T> PageResult<T> from(Page<T> page) {
		return new PageResult<>(
				page.getContent(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages(),
				page.isFirst(),
				page.isLast());
	}

	public static <T> PageResult<T> of(List<T> content, Integer pageNumber, Integer pageSize, Long totalElements,
			Integer totalPages) {
		return new PageResult<>(
				content,
				pageNumber,
				pageSize,
				totalElements,
				totalPages,
				pageNumber == 0,
				pageNumber + 1 == totalPages);
	}

	public static <T, S> PageResult<T> from(PageResult<S> pageResult, Function<S, T> mapper) {
		return new PageResult<>(
				pageResult.content.stream().map(mapper).toList(),
				pageResult.pageNumber,
				pageResult.pageSize,
				pageResult.totalElements,
				pageResult.totalPages,
				pageResult.first,
				pageResult.last);
	}
}
