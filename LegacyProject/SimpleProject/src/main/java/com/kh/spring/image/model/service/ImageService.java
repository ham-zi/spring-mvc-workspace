package com.kh.spring.image.model.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.exception.AuthorizationException;
import com.kh.spring.image.model.dao.ImageMapper;
import com.kh.spring.image.model.dto.AttachmentDto;
import com.kh.spring.image.model.dto.ImageDto;
import com.kh.spring.member.model.dto.MemberDto;
import com.kh.spring.util.PageInfo;
import com.kh.spring.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageMapper imageMapper;
	private final Pagination pagination;
	
	public int imagesCount() {
		return imageMapper.imagesCount();
	}
	
	public Map<String, Object> findImages(int page) {

		//게시글을 총 개수
		int listCount = imagesCount();
		//log.info("게시글 총 개수 : {}", listCount);
		PageInfo pi = pagination.getPageInfo(listCount, page, 3, 3);
		RowBounds rb = new RowBounds((page-1)*3,3);
		
		List<ImageDto>images = imageMapper.findImages(rb);
		for(ImageDto image : images) {
			//log.info("image:{}",image);
			if("Y".equals(image.getImageExist())) {
				AttachmentDto at = findAttachment(image.getImageNo());
				if(at != null) {
					String src= at.getFilePath()+"/"+at.getChangeName();
					image.setSrc(src);
				}
			}
		}
		
		Map<String, Object> map = Map.of("images",images,"pi",pi);
		return map;
	}
	
	private AttachmentDto findAttachment(Long imageNo) {
		return imageMapper.findAttachment(imageNo);
	}
	

	@Transactional
	public void insertImage(List<MultipartFile> upfile, HttpSession session, ImageDto image) {
		// 정상적인 접근인가?
		// 글쓴이와 세션이 같은 사람인가?
		checkAuthorization(session, image);
		//보드 인서트를 한다
		//보드가 성공하면 이미지 저장 및 인서트를 한다
		//log.info("multi:{}",upfile);
		if(upfile.get(0).getName().isEmpty()) { //없으면
			image.setImageExist("N");
			imageMapper.insertImage(image);
			
		
		} else { //있으면
			image.setImageExist("Y");
			int result = imageMapper.insertImage(image);
			if(result > 0) {
				for(MultipartFile multipartFile : upfile) {
					if(multipartFile.isEmpty()) continue;
					
					String changeName = rename(multipartFile);
					String originName = multipartFile.getOriginalFilename();
					String savePath = fileTransferTo(multipartFile, session, changeName);
					AttachmentDto at = new AttachmentDto();
					
					at.setFilePath(savePath);
					at.setChangeName(changeName);
					at.setOriginName(originName);
					at.setRefIno(image.getImageNo());
					at.setFileLevel("C");
					if(multipartFile.getName().equals("mainUpfile")) {
						at.setFileLevel("M");
					}
					
					
					int atResult = imageMapper.insertAttachment(at);
					if(atResult != 1) {
						//파일 삭제
						new File(savePath + "/" + at.getChangeName()).delete();
					}
				}	
			}
		}
		//imageMapper.insertImage(image);
		//이미지까지 성공했으면
		//새로운 게시글리스트를 반환해준다.
		
	}
	
	private String fileTransferTo(MultipartFile upfile, HttpSession session, String changeName) {
		ServletContext application = session.getServletContext();
		String savePath = application.getRealPath("/resources/imageFiles/");
		
		try {
			upfile.transferTo(new File(changeName));
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return savePath;
	}
	
	private String rename(MultipartFile upfile) {
		StringBuilder sb = new StringBuilder();
		sb.append("KH_");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		sb.append("_");
		sb.append((int)(Math.random()*900)+100);
		sb.append(upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf(".")));
		return sb.toString();
	}

	
	
	private void checkAuthorization(HttpSession session, ImageDto image) {
		MemberDto userInfo = ((MemberDto)session.getAttribute("userInfo"));
		if(!(image.getImageWriter().equals(userInfo.getUserId()))) {
			throw new AuthorizationException("정상적인 접근이 아닙니다.");
		}
	}
	
}
