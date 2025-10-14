package com.dlut.simpleforum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dlut.simpleforum.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	@Query("""
			SELECT b
			FROM Board b
			WHERE b.name LIKE %:pattern% OR
				b.description LIKE %:pattern%
			""")
	Page<Board> findByNameOrDescriptionContaining(String pattern, Pageable pageable);
}
