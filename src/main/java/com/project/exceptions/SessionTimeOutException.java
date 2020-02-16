package com.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SessionTimeOutException extends Exception{
	
	private final String message;

	public SessionTimeOutException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
