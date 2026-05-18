package com.kh.spring.member.model.service;

import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dto.MemberDto;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Override
	public void login(MemberDto member) {
		System.out.println("서비스 연결 성공");
	}
}
