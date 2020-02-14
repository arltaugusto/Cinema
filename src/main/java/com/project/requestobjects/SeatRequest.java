package com.project.requestobjects;

import com.project.entities.PlayPK;

public class SeatRequest {
	
	private int seatId;
	private int roomId;
	private String userId;
	private PlayPK playPk;

	public PlayPK getPlayPk() {
		return playPk;
	}

	public void setPlayPk(PlayPK playPk) {
		this.playPk = playPk;
	}

	public SeatRequest() {}
	
	public SeatRequest(int seatId, int roomId, String userId, PlayPK playPk) {
		this.seatId = seatId;
		this.roomId = roomId;
		this.userId = userId;
		this.playPk = playPk;
	}
	
	public int getSeatId() {
		return seatId;
	}
	
	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	public int getRoomId() {
		return roomId;
	}
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
