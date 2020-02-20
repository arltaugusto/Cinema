package com.project.requestobjects;

import java.util.ArrayList;
import java.util.List;

import com.project.entities.Booking;

public class UserDTO {
	
	private String userId;
	private String email;
	private String name;
	
	
	private String password;
	
	private List<Booking> books = new ArrayList<>();
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public List<Booking> getBooks() {
		return books;
	}
	
	public void setBooks(List<Booking> books) {
		this.books = books;
	}
}
