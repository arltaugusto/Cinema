package com.project.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SeatPK implements Serializable{
	private int roomId;
	private int seatId;
	
	public SeatPK() {}
	public SeatPK(int roomId, int seatId) {
		super();
		this.roomId = roomId;
		this.seatId = seatId;
	}
	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getSeatId() {
		return seatId;
	}
	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roomId;
		result = prime * result + seatId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatPK other = (SeatPK) obj;
		if (roomId != other.roomId)
			return false;
		if (seatId != other.seatId)
			return false;
		return true;
	}
	
}
