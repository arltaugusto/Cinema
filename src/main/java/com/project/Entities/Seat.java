package com.project.Entities;

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
	@MapsId("salaId")
	@ManyToOne
	private Sala sala;
	 
	@ManyToMany
	@JsonIgnore
	private List<Booking> books = new ArrayList<>();
	 

	public SeatPK getSeatPk() {
		return seatPk;
	}

	public void setSeatPk(SeatPK seatPk) {
		this.seatPk = seatPk;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public List<Booking> getBooks() {
		return books;
	}

	public void setBooks(List<Booking> books) {
		this.books = books;
	}

}
