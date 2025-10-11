package com.dlut.simpleforum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlut.simpleforum.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
