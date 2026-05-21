package com.kh.spring.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {
	private int listCount;
	private int currentPage;
	private int boardLimit;
	private int pageLimit;
	private int maxPage;
	private int startPage;
	private int endPage;
}
