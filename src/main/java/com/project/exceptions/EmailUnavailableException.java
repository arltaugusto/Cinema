package com.project.exceptions;

import com.project.Entities.User;

public class EmailUnavailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public EmailUnavailableException(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return String.format("%s is already used", user.getEmail());
	}

}
