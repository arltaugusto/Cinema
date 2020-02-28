package com.project.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "movies")
public class Movie {
	
	@Id
	private String movieId;
	
	private String name;
	private long duration;
	private String imagePath;
	private String synopsis;
	private boolean isActive;
	
	
	@OneToMany(mappedBy = "movie")
	@JsonIgnore
	private List<Play> plays = new ArrayList<>();
	
	public Movie() {}

	public Movie( String name, long duration, String synopsis, boolean isActive) {
		this.movieId = UUID.randomUUID().toString().replace("-", StringUtils.EMPTY); 
		this.name = name;
		this.duration = duration;
		this.synopsis = synopsis;
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public String getMovieId() {
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

	public void setMovieId(String id) {
		this.movieId = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
