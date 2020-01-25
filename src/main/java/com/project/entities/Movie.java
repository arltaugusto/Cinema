package com.project.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "movies")
public class Movie {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private long duration;
	private String imagePath;
	
	@OneToMany(mappedBy = "movie")
	@JsonIgnore
	private List<Play> plays = new ArrayList<>();
	
	public Movie() {}

	public Movie(String name, long duration, String imagePath) {
		this.name = name;
		this.duration = duration;
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getid() {
		return id;
	}

	public List<Play> getPlays() {
		return plays;
	}
	
	public void setPlays(List<Play> plays) {
		this.plays = plays;
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
