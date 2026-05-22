package com.kh.spring.image.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.image.model.dto.ImageDto;
import com.kh.spring.image.model.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ImageController {
	
	private final ImageService service;
	
	@GetMapping("images")
	public ModelAndView toImages(ModelAndView mv ,@RequestParam(value="page", defaultValue="1")int page) {

		Map<String,Object> map = null;
		map = service.findImages(page);
		mv.addObject("map", map).setViewName("/image/images");
		return mv;
	}
	
	@GetMapping("image/form")
	public String toImageForm() {
		return "image/form";
	}
	
	@PostMapping("images")
	public String insertImage(@RequestParam(value="upfile")List<MultipartFile>upfile, HttpSession session, ImageDto image) {
		service.insertImage(upfile, session, image);
		return "redirect:images";
	}
	
}
