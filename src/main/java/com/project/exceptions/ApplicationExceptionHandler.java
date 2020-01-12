package com.project.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest webRequest) {
		ApplicationErrors errors = new ApplicationErrors(ex.getMessage(), "401");
		errors.setDate(new Date());
		return new ResponseEntity<ApplicationErrors>(errors, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleNoTimeAvailableException(NoTimeAvailableException ex, WebRequest webRequest) {
		return badRequestException(ex, webRequest);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleSeatAlreadyBookedException(SeatAlreadyBookedException ex, WebRequest webRequest) {
		return badRequestException(ex, webRequest);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleNoSeatBookedException(NoSeatBookedException ex, WebRequest webRequest) {
		return badRequestException(ex, webRequest);
	}
	
	private ResponseEntity<ApplicationErrors> badRequestException(Exception ex, WebRequest webRequest) {
		ApplicationErrors errors = new ApplicationErrors(ex.toString(), "400");
		errors.setDate(new Date());
		return new ResponseEntity<ApplicationErrors>(errors, HttpStatus.BAD_REQUEST);
	}
}
