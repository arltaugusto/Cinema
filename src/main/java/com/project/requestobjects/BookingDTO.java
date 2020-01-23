package com.project.requestobjects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToMany;

import com.project.entities.Play;
import com.project.entities.Seat;
import com.project.entities.User;

public class BookingDTO {

	private int bookId;
	private LocalDateTime bookDate;
	
	private User user;
	
	private Play play;
	
	@ManyToMany
	private List<Seat> seats = new ArrayList<>();
	
	public BookingDTO() {}
	
	public BookingDTO(User user, Play play, List<Seat> seats) {
		this.user = user;
		this.play = play;
		this.seats = seats;
		this.bookDate = LocalDateTime.now();
	}
	
	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public LocalDateTime getBookDate() {
		return bookDate;
	}

	public void setBookDate(LocalDateTime bookDate) {
		this.bookDate = bookDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
