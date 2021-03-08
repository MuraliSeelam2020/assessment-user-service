package com.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AppException.class)
	public final ResponseEntity<String> handleAllExceptions(AppException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<AppException> handleAllExceptions(Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);

		return new ResponseEntity<>(new AppException(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

}
