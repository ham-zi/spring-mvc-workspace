package com.kh.spring.exception.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.exception.InvalidParameterException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(InvalidParameterException.class)
	protected ModelAndView invalidParamterError(InvalidParameterException e) {
		log.info("{}", e.getMessage());
		ModelAndView mv = new ModelAndView();
		mv.addObject("mssage", e.getMessage()).setViewName("include/error_page");
		return mv;
	}
	
}
