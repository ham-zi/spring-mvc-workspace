package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
		if(member.getUserId() == null ||
			member.getUserId().trim().isEmpty() ||
			member.getUserPwd() == null ||
			member.getUserPwd().trim().isEmpty() ||
			member.getUserName() == null ||
			member.getUserName().trim().isEmpty()) {
			throw new InvalidParameterException("유효하지 않는 값입니다.");
		}
				
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
}
