package com.dlut.simpleforum.dto.result;

import java.util.ArrayList;
import java.util.List;

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
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean first;
	private boolean last;

	public static <T> PageResult<T> from(Page<T> page) {
		return new PageResult<>(
				new ArrayList<>(page.getContent()),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages(),
				page.isFirst(),
				page.isLast());
	}
}
