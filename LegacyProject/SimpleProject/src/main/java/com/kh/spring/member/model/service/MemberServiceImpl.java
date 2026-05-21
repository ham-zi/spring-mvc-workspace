package com.kh.spring.member.model.service;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.spring.exception.AuthorizationException;
import com.kh.spring.exception.InvalidParameterException;
import com.kh.spring.exception.TooLargeValueException;
import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.dao.MemberMapper;
import com.kh.spring.member.model.dto.MemberDto;
import com.kh.spring.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl{
	
	//private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
	//private final MemberDao memberDao;
	//private final SqlSessionTemplate sqlSession;
	
	private final MemberMapper memberMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	//private final PasswordEncoder passwordEncoder;
	
	/*
	@Autowired
	public MemberServiceImpl(MemberDao memberDao, SqlSessionTemplate sqlSession) {
		this.memberDao = memberDao;
		this.sqlSession = sqlSession;
	}
	
	스프링의 거의 모든 기능은 "개발자가 직접하던 귀찮은 일을 컨테이너가 대신 해준다."

	*/
	public MemberDto login(MemberDto member) {
		//validateMember(member);
		// TRACE, DEBUG, INFO, WARN, ERROR
		//System.out.println("서비스 연결 성공" + member);
		//log.info("인포 메서드로 출력 {}", member);
		
		// Login -> ver_1 
		/*
		 * SqlSession session = Template.getSqlSession();
		 * MemberDto login = new MemberDao().login(session, member);
		 * session.close();
		 * return login;
		 */
		//return memberDao.login(sqlSession, member);
		
		
		
		
		MemberDto userInfo = memberMapper.login(member);
		
		if(userInfo == null) {
			throw new InvalidParameterException("아이디 또는 비밀번호가 틀림");
		}
		
		//1절
		//log.info("사용자가 입력한 비밀번호 평문 : {}", member.getUserPwd());
		//log.info("DB에 저장된 암호화된 암호문 : {}", userInfo.getUserPwd());
		if(passwordEncoder.matches(member.getUserPwd(), userInfo.getUserPwd())) {
			return userInfo;
		}
		
		
		return null;
	}
	
	public void signup(MemberDto member) {
		validateMember(member);
		
		/*
		if(member.getUserId().length() > 30) {
			throw new TooLargeValueException("아이디 값이 너무 깁니다.");
		}
		
		if(member.getUserId() == null ||
		   member.getUserId().trim().isEmpty() ||
		   member.getUserPwd() == null ||
		   member.getUserPwd().trim().isEmpty() ||
		   member.getUserName() == null ||
		   member.getUserName().trim().isEmpty()) {
			throw new InvalidParameterException("유효하지 않는 값입니다.");
		}
		*/
		//사용자입력값
		//정책
		//평문 = plain text
		String plainPwd = member.getUserPwd();
		String encPwd = passwordEncoder.encode(plainPwd);
		
		
		//이 상황에서 VO를 사용함
		//예외처리를 끝냈고, 불변해야할 값들이기 때문이다.(불변성보장)
		//Member encMember = new Member(member.getUserId(), encPwd, member.getUserName(), member.getEmail(), null, null, null);
		Member encMember = Member.builder()
				                 .userId(member.getUserId())
				                 .userPwd(encPwd)
				                 .userName(member.getUserName())
				                 .email(member.getEmail())
				                 .build();
		log.info("{}의 암호문 :{}",plainPwd,encPwd);
		//응? 그럼 그냥 null로 넣는거랑 뭐가 다른거지?
		//오히려 할 일이 더 늘어난거 아닌가?
		//가독성은 너무 좋긴한데...
		
		memberMapper.signup(encMember);
		
		
		//memberMapper.signup(member);
		//return validateLogin Member(userInfo, member.getUserId());
	}
	
	private void validateMember(MemberDto member) {
		checkLength(member);
		checkBlank(member);
	}
	
	private void checkLength(MemberDto member) {
		if(member.getUserId().length() > 20) {
			throw new TooLargeValueException("아이디 값이 넘 깁니다.");
		}
	}
	
	private void checkBlank(MemberDto member) {
		checkUserPwd(member);
		checkUserId(member);
		checkUserName(member);
	}
	
	private void checkUserId(MemberDto member) {
		if(member.getUserId() == null ||
		   member.getUserId().trim().isEmpty()) {
			throw new InvalidParameterException("유효하지 않는 값입니다.");

		}
	}
	private void checkUserPwd(MemberDto member) {
		if(member.getUserPwd() == null ||
		   member.getUserPwd().trim().isEmpty()){
				throw new InvalidParameterException("유효하지 않는 값입니다.");
		}
	}
	private void checkUserName(MemberDto member) {
		if(
			member.getUserName() == null ||
			member.getUserName().trim().isEmpty()) {
			throw new InvalidParameterException("유효하지 않는 값입니다.");
		}
	}
	
	private void checkNull(MemberDto member) {
		if(member == null) {
			throw new NullPointerException("잘못된 접근입니다.");
		}
	}
	
	private void validateUpdateMember(MemberDto member, MemberDto sessionMember) {
		checkNull(member);
		checkNull(sessionMember);
		checkUserName(member);
		checkUserId(member);
		if(!member.getUserId().equals(sessionMember.getUserId())) {
			throw new AuthorizationException("권한없는 접근입니다.");
		}
		checkNull(memberMapper.login(member));
	}
	
	
	
	/*
	 * SRP(Single Responsibility Principle)
	 * 단일 책임 원칙
	 * 하나의 클래스(메소드)는 하나의 책임만을 가져야한다.
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
	/*
	private MemberDto validateLoginMember(MemberDto loginMember, String userPwd) {
		if(userInfo == null) {
			throw new InvalidParameterException("아이디 또는 비밀번호가 틀림");
			
		}
		if(passwordEncoder.matches(userPwd, userPwd())){
			return userInfo;
			
		}
		return null;
		
	}
	*/
	
	public void update(MemberDto member, HttpSession session) {
		
		MemberDto sessionMember = ((MemberDto)session.getAttribute("userInfo"));
		
		// 구체적으로 생각해야한다
		//앞단에서 넘어온 아이디값과 로그인된 사용자의 ID값이 일치하는가?
		//실제 DB에 ID값이 존재하는 회원인가?
		//USERNAME컬럼에 넣을 값이 USERNAME컬럼크기보다 크지 않은가?
		//EMAIL컬럼에 넣을 값이 EMAIL컬럼 크기보다 크지 않은가?
		//빈문자열 등등
		
		
		// DB가서 UPDATE
		
		// 업데이트가 성공적으로 수행되었는가?
		// 수정된 정보를 DB에서 SELECT => sessionScope에 존재하는 userInfo 키값의 MemberDto객체 필드값을 갱신
		validateUpdateMember(member, sessionMember);
		int result = memberMapper.update(member);
		
		if(result != 1) {
			throw new AuthorizationException("문제가 발생했습니다. 관리자에게 문의하세요");
		}
		sessionMember.setUserName(member.getUserName());
		sessionMember.setEmail(member.getEmail());
	}
	
	public void delete(String userId, String userPwd, HttpSession session) {
		
		MemberDto sessionMember = ((MemberDto)session.getAttribute("userInfo"));
		checkNull(sessionMember);
		if(!userId.equals(sessionMember.getUserId())) {
			throw new AuthorizationException("권한없는 접근입니다.");
		}
		
		String encPassword = memberMapper.login(sessionMember).getUserPwd();
		if(!passwordEncoder.matches(userPwd, encPassword)) {
			throw new AuthorizationException("비밀번호가 일치하지 않습니다.");
		}
		
		int result = memberMapper.delete(userId);
		
		if(result != 1) {
			throw new AuthorizationException("관리자에게 문의하세요");
		}
		
		session.removeAttribute("userInfo");
		
	}
	public String checkId(String id) {
		return memberMapper.checkId(id);
	}
	
	
	
}
