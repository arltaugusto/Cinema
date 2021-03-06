package com.project.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.utils.SeatManager;

@Entity
@Table(name = "bookings")
public class Booking implements SeatManager {
	
	@Id
	@GeneratedValue
	private int bookId;
	private LocalDateTime bookDate;
	
	@ManyToOne
	@JoinColumn(name="userId")
	@JsonIgnore
	private User user;
	
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="movie_id", referencedColumnName = "movie_id"),
			@JoinColumn(name="room_id", referencedColumnName = "room_id"),
			@JoinColumn(name="startTime", referencedColumnName = "startTime")
	})
	private Play play;
	
	@ManyToMany
	private List<Seat> seats = new ArrayList<>();
	
	private double total;
	
	public Booking() {}
	
	public Booking(User user, Play play, List<Seat> seats, double total) {
		this.user = user;
		this.play = play;
		this.seats = seats;
		this.bookDate = LocalDateTime.now();
		this.total = total;
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
