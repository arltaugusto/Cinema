package com.project.requestobjects;

import java.util.ArrayList;
import java.util.List;

import com.project.entities.PlayPK;

public class BookRequestDTO {
	
	private String userId;
	private PlayPK playPk;
	
	public PlayPK getPlayPk() {
		return playPk;
	}
	public void setPlayPk(PlayPK playPk) {
		this.playPk = playPk;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
