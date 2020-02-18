package com.project.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PlayPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "movie_id")
	private int movieId;
	
	@Column(name = "room_id")
	private int roomId;
	
	@Column(name = "startTime")
	private LocalDateTime startTime;
	
	public PlayPK() {}
	
	public PlayPK(int movieId, int roomId, LocalDateTime startTime) {
		this.movieId = movieId;
		this.roomId = roomId;
		this.startTime = startTime;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + movieId;
		result = prime * result + roomId;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		PlayPK other = (PlayPK) obj;
		if (movieId != other.movieId)
			return false;
		if (roomId != other.roomId)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
					return false;
		return true;
	}
}
