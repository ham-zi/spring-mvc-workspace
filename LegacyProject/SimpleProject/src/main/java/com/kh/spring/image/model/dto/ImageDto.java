package com.kh.spring.image.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ImageDto {
	private Long imageNo;
	private String imageWriter;
	private String imageTitle;
	private String imageContent;
	private int count;
	private Date createDate;
	private String imageExist;
	private String status;
	private String src;
}
