package com.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.entities.Play;
import com.project.entities.PlayPK;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoTimeAvailableException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private final transient Play play;

	public NoTimeAvailableException(Play play) {
		super();
		this.play = play;
	}

	public Play getPlay() {
		return play;
	}

	@Override
	public String toString() {
		PlayPK playPk = play.getPlayPK();
		return String.format("Not Available time for %s at %s in %s room", play.getMovie().getName(), playPk.getStartTime().toString(), playPk.getSalaId());
	}
}
