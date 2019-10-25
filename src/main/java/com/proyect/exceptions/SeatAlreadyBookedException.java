package com.proyect.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.proyect.Entities.Seat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatAlreadyBookedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Seat seat;

	public SeatAlreadyBookedException(Seat seat) {
		super();
		this.seat = seat;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}
	
	@Override
	public String toString() {
		return String.format("The seat %d is already booked!", seat.getSeatPk().getSeatId());
	}
}
