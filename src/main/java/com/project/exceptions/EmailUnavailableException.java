package com.project.exceptions;

public class EmailUnavailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	public String getUser() {
		return email;
	}
	public void setUser(String email) {
		this.email = email;
	}
	public EmailUnavailableException(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return String.format("%s is already used", email);
	}

}
