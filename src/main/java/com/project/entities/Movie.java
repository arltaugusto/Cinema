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
	private int movieId;
	
	private String name;
	private long duration;
	private String imagePath;
	private String synopsis;
	
	
	@OneToMany(mappedBy = "movie")
	@JsonIgnore
	private List<Play> plays = new ArrayList<>();
	
	public Movie() {}

	public Movie(String name, long duration, String imagePath, String synopsis) {
		this.name = name;
		this.duration = duration;
		this.imagePath = imagePath;
		this.synopsis = synopsis;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getMovieId() {
		return movieId;
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

	public void setMovieId(int id) {
		this.movieId = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
