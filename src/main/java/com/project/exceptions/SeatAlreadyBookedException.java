package com.project.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.entities.Seat;
import com.project.entities.SeatPK;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatAlreadyBookedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final transient  List<Seat> busySeats;

	public SeatAlreadyBookedException(List<Seat> seats) {
		super();
		this.busySeats = seats;
	}

	public List<Seat> getSeat() {
		return busySeats;
	}

	@Override
	public String toString() {
		return String.format("%s already booked",
				busySeats.stream()
					.map(Seat::getSeatPk)
					.map(SeatPK::getSeatId)
					.map(String::valueOf)
					.collect(Collectors.joining(", ")));
	}
}
