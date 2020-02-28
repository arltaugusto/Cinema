package com.project.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "plays")
public class Play {
	
	@EmbeddedId
	private PlayPK playPK;
	
	private LocalDateTime endTime;
	private int availableSeats;
	
	@MapsId("movieId")
	@ManyToOne
	@JoinColumn(name = "movie_id")
	private Movie movie;
	
	@MapsId("roomId")
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	@OneToMany(mappedBy = "play")
	private List<Booking> books = new ArrayList<>();
	
	private boolean isActive;
	
	public Play() {}
	
	public Play(PlayPK playPK, LocalDateTime endTime, int availableSeats, Movie movie, Room room, boolean isActive) {
		this.playPK = playPK;
		this.endTime = endTime;
		this.availableSeats = availableSeats;
		this.movie = movie;
		this.room = room;
		this.isActive = isActive;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public PlayPK getPlayPK() {
		return playPK;
	}

	public void setPlayPK(PlayPK playPK) {
		this.playPK = playPK;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Booking> getBooks() {
		return books;
	}

	public void setBooks(List<Booking> books) {
		this.books = books;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
