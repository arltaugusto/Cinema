package com.project.requestobjects;

import java.time.LocalDateTime;
import java.util.List;

import com.project.entities.PlayPK;
import com.project.entities.Seat;
import com.project.utils.SeatManager;

public class TemporalSeats implements SeatManager {
	
	private List<Seat> seats;
	private String userId;
	private PlayPK playPk;
	private LocalDateTime initTime;
	private boolean isOpen;
	
	public TemporalSeats() {}

	public TemporalSeats(List<Seat> seat, String userId, PlayPK play, LocalDateTime initTime) {
		this.seats = seat;
		this.userId = userId;
		this.playPk = play;
		this.initTime = initTime;
		this.isOpen = true;
	}
	
	public LocalDateTime getInitTime() {
		return initTime;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public void setInitTime(LocalDateTime initTime) {
		this.initTime = initTime;
	}

	public List<Seat> getSeats() {
		return seats;
	}
	
	public void setSeats(List<Seat> seat) {
		this.seats = seat;
	}
	
//	public String getUserId() {
//		return userId;
//	}
//	
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
	
	public void addSeat(Seat seat) {
		seats.add(seat);
	}
	
	public PlayPK getPlayPk() {
		return playPk;
	}
	
	public void setPlayPk(PlayPK play) {
		this.playPk = play;
	}
	
	public void clearSeats() {
		seats.clear();
	}
	
	public void removeSeat(Seat seat) {
		seats.remove(seat);
	}
	
}
