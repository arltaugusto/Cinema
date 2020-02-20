package com.project.utils;

import java.time.LocalDateTime;

import com.project.entities.SeatPrice;

public class MaxSeatPrice {
	private SeatPrice seatPrice;
	private LocalDateTime date;
	
	public MaxSeatPrice() {}
	
	public MaxSeatPrice(SeatPrice seatPrice, LocalDateTime date) {
		super();
		this.seatPrice = seatPrice;
		this.date = date;
	}
	
	public SeatPrice getSeatPrice() {
		return seatPrice;
	}
	
	public void setSeatPrice(SeatPrice seatPrice) {
		this.seatPrice = seatPrice;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
