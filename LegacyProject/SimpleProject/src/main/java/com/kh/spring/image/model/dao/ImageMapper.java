package com.kh.spring.image.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.image.model.dto.AttachmentDto;
import com.kh.spring.image.model.dto.ImageDto;

@Mapper
public interface ImageMapper {
	
	int imagesCount();
	List<ImageDto> findImages(RowBounds rb);
	AttachmentDto findAttachment(Long refIno);
	int insertImage(ImageDto image);
	int insertAttachment(AttachmentDto at);
}
