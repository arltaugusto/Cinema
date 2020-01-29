package com.project.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "salas")
public class Room {
	
	@Id
	@GeneratedValue
	private int id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sala")
	private List<Play> plays = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "sala")
	private List<Seat> seats = new ArrayList<>();

	public List<Play> getPlays() {
		return plays;
	}

	public void setPlays(List<Play> plays) {
		this.plays = plays;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
