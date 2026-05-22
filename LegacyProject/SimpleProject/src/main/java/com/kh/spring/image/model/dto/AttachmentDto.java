package com.kh.spring.image.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
	private Long attachmentNo;
	private Long refIno;
	private String originName;
	private String changeName;
	private String filePath;;
	private String fileLevel;
}
