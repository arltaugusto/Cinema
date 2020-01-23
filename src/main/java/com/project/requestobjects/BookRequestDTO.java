package com.project.requestobjects;

import java.util.ArrayList;
import java.util.List;

import com.project.entities.PlayPK;

public class BookRequestDTO {
//	private Book book;
	private String userId;
	private PlayPK playPk;
	private List<Integer> seats = new ArrayList<>();
	
	public List<Integer> getSeats() {
		return seats;
	}
	public void setSeats(List<Integer> seats) {
		this.seats = seats;
	}
	public PlayPK getPlayPk() {
		return playPk;
	}
	public void setPlayPk(PlayPK playPk) {
		this.playPk = playPk;
	}
//	public Book getBook() {
//		return book;
//	}
//	public void setBook(Book book) {
//		this.book = book;
//	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
