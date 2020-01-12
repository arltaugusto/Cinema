package com.project.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.project.utils.IdGenerator;


@Entity
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(generator = IdGenerator.generatorName)
    @GenericGenerator(name = IdGenerator.generatorName, strategy = "com.project.utils.IdGenerator")
	private String userId;
	private String email;
	private String name;
	private Boolean isAdmin;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@OneToMany(mappedBy = "user")
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
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getId() {
		return userId;
	}
	public void setId(String userId) {
		this.userId = userId;
	}
	public List<Booking> getBooks() {
		return books;
	}
	public void setBooks(List<Booking> books) {
		this.books = books;
	}
}
