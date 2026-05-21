package com.kh.spring.exception.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.exception.AuthorizationException;
import com.kh.spring.exception.InvalidParameterException;
import com.kh.spring.exception.TooLargeValueException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

	private ModelAndView createErrorResponse(RuntimeException e) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("mssage", e.getMessage()).setViewName("include/error_page");
		return mv;
	}
	
	@ExceptionHandler(TooLargeValueException.class)
	protected ModelAndView TooLargeValueError(TooLargeValueException e) {
		return createErrorResponse(e);
	}
	
	@ExceptionHandler(InvalidParameterException.class)
	protected ModelAndView invalidParamterError(InvalidParameterException e) {
		log.info("{}", e.getMessage());
		return createErrorResponse(e);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	protected ModelAndView authorizationError(AuthorizationException e) {
		return createErrorResponse(e);	
	}
	
}
