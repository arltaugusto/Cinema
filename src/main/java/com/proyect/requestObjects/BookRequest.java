package com.proyect.requestObjects;

import java.util.ArrayList;
import java.util.List;

import com.proyect.Entities.Book;
import com.proyect.Entities.PlayPK;

public class BookRequest {
	private Book book;
	private int userId;
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
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
