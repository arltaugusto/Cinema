package com.project.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "seats")
public class Seat {
	
	@EmbeddedId
	private SeatPK seatPk;
	
	@JsonIgnore
	@MapsId("roomId")
	@ManyToOne
	private Room room;
	 
	@ManyToMany
	@JsonIgnore
	private List<Booking> books = new ArrayList<>();
	 
	public Seat() {}
	
	public Seat(SeatPK seatPk, Room room) {
		this.seatPk = seatPk;
		this.room = room;
	}

	public SeatPK getSeatPk() {
		return seatPk;
	}

	public void setSeatPk(SeatPK seatPk) {
		this.seatPk = seatPk;
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
	
}
