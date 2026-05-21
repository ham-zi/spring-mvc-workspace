package com.kh.spring.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.BoardDto;

@Mapper
public interface BoardMapper {
	
	int selectTotalCount();
	List<BoardDto> findAll(RowBounds rb);
	int save(BoardDto board);
	int increaseCount(Long boardNo);
	BoardDto findByBoardNo(Long boardNo);
	int findByKeywordCount(@Param(value="condition") String condition,@Param(value="keyword") String keyword);
	List<BoardDto> findByKeyword(@Param(value="condition") String condition, @Param(value="keyword") String keywordm, RowBounds rb);
}
