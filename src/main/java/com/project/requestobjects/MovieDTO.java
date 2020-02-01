package com.project.requestobjects;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.entities.Play;

public class MovieDTO {

	private int id;
	
	private String name;
	private long duration;
	private String synopsis;
	
	private List<Play> plays = new ArrayList<>();
	
	public MovieDTO() {}

	public MovieDTO(int id, String name, long duration, String synopsis) {
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.synopsis = synopsis;
	}

	public int getId() {
		return id;
	}

	public List<Play> getPlays() {
		return plays;
	}
	
	public void setPlays(List<Play> plays) {
		this.plays = plays;
	}
	
	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setid(int id) {
		this.id = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}