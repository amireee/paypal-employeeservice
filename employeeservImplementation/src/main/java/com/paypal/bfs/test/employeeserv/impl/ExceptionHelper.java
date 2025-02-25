package com.paypal.bfs.test.employeeserv.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.paypal.bfs.test.employeeserv.exception.BusinessException;
import com.paypal.bfs.test.employeeserv.exception.InvalidInputException;

@ControllerAdvice
public class ExceptionHelper {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

	@ExceptionHandler(value = { InvalidInputException.class })
	public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
		logger.error("Invalid Input Exception: ", ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = { BusinessException.class })
	public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
		logger.error("Business Exception: ", ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		logger.error("Exception: ", ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
