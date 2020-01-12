package com.project.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.Entities.Seat;
import com.project.Entities.SeatPK;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatAlreadyBookedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Seat> busySeats = new ArrayList<>();

	public SeatAlreadyBookedException(List<Seat> seats) {
		super();
		this.busySeats = seats;
	}

	public List<Seat> getSeat() {
		return busySeats;
	}

	public void setSeat(List<Seat> seat) {
		this.busySeats = seat;
	}
	
	@Override
	public String toString() {
		return String.format("%s already booked",
				busySeats.stream()
					.map(Seat::getSeatPk)
					.map(SeatPK::getSeatId)
					.map(id -> Integer.toString(id))
					.collect(Collectors.joining(", ")));
	}
}
