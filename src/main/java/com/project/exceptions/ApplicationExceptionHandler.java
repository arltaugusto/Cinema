package com.project.exceptions;

import java.time.LocalDateTime;

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
		return clientException(ex, webRequest, "401", HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleEmailUnavailableException(EmailUnavailableException ex, WebRequest webRequest) {
		return clientException(ex, webRequest, "401", HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleNoTimeAvailableException(NoTimeAvailableException ex, WebRequest webRequest) {
		return clientException(ex, webRequest, "400", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleSeatAlreadyBookedException(SeatAlreadyBookedException ex, WebRequest webRequest) {
		return clientException(ex, webRequest, "400", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApplicationErrors> handleNoSeatBookedException(NoSeatBookedException ex, WebRequest webRequest) {
		return clientException(ex, webRequest, "400", HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<ApplicationErrors> clientException(Exception ex, WebRequest webRequest, String code, HttpStatus httpStatus) {
		ApplicationErrors errors = new ApplicationErrors(ex.toString(), code);
		errors.setDate(LocalDateTime.now());
		return new ResponseEntity<ApplicationErrors>(errors, httpStatus);
	}
}
