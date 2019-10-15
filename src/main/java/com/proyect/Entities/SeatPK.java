package com.proyect.Entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SeatPK implements Serializable{
	private int salaId;
	private int seatId;
	
	public SeatPK() {}
	public SeatPK(int salaId, int seatId) {
		super();
		this.salaId = salaId;
		this.seatId = seatId;
	}
	public int getSalaId() {
		return salaId;
	}

	public void setSalaId(int salaId) {
		this.salaId = salaId;
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
		result = prime * result + salaId;
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
		if (salaId != other.salaId)
			return false;
		if (seatId != other.seatId)
			return false;
		return true;
	}
	
}
