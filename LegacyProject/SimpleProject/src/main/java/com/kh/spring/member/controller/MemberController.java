package com.kh.spring.member.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.kh.spring.member.model.dto.MemberDto;
import com.kh.spring.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor // 생성자 주입용 매개변수 생성자를 생성해주는 Lombok 애노테이션
public class MemberController {
	
	//@Autowired
	private final MemberService memberService;
	
	//@Autowired
	//public void setMemberService(MemberService memberService) {
	//	this.memberService = memberService;
	//}
	/*
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	*/
	//생성자 주입
	@RequestMapping("login")
	public String login(MemberDto member) {
		
		memberService.login(member);
		
		return "main";
	}

	//public MemberController() {
	//	System.out.println("빈 등록 성공");
	//}
	
	// 1. 값뽑기
	// 2. 가공
	/*@RequestMapping("login")
	public String login(HttpServletRequest request) {
		String userId = request.getParameter("id");
		String userPwd = request.getParameter("pwd");
		
		System.out.printf("id : %s, pwd: %s", userId, userPwd);
		return "main";
	}
	
	@RequestMapping("login")
	public String login(@RequestParam(value="id")String userId,@RequestParam(value="pwd") String userPwd) {
		System.out.printf("id : %s, pwd: %s", userId, userPwd);
		return "main";
	}	
	
	@RequestMapping("login")
	public String login(String id, String pwd) {
		System.out.printf("id : %s, pwd: %s", id, pwd);
		return "main";
	} //권장하지 않는 방식
	 */

	/*
	 * HandlerAdapter의 판단 : 
	 * 1. 매개변수 자리에 기본타입 (int, boolean, String, Date 등)이 있거나
	 *    @RequestParam애노테이션이 적혀있는 경우 ==> @RequestParam으로 인식
	 * 2. 매개변수 자리에 사용자 정의 클래스(MemberDto, Board, Reply, ...)이 있거나
	 *    @ModelAttribute애노테이션이 존재하는 경우 ==> 커맨드객체 방식으로 인식
	 * 
	 * 커맨드객체 방식: 스프링에서 디스패쳐서블릿이 ???
	 * 스프링에서 해당 객체를 기본 생성자를 이용해서 생성한 후 내부적으로 setter메서드를 찾아서
	 * 요청 시 전달 값을 해당 필드에 대입해준다.
	 * 
	 * 1. 매개변수 자료형에 반드시 기본생성자가 존재할 것
	 * 2. setter메서드가 반드시 존재할 것
	 * 3. 전달되는 키값과 객체의 필드명이 동일할 것
	 * 
	 */
	
	
	
	
	
	
}
