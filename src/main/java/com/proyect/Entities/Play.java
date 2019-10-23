package com.proyect.Entities;

import java.util.ArrayList;
import java.util.Date;
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
	
	private Date endTime;
	private int availableSeats;
	
	@MapsId("MovieId")
	@ManyToOne
	@JoinColumn(name = "movie_id")
	private Movie movie;
	
	@MapsId("playId")
	@ManyToOne
	@JoinColumn(name = "sala_id")
	private Sala sala;

	@OneToMany(mappedBy = "play")
	private List<Book> books = new ArrayList<>();
	public Play() {}
	
	public Play(PlayPK playPK, Date endTime, int availableSeats, Movie movie, Sala sala) {
		super();
		this.playPK = playPK;
		this.endTime = endTime;
		this.availableSeats = availableSeats;
		this.movie = movie;
		this.sala = sala;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
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

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
