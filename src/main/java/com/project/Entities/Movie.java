package com.project.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private long duration;
	
	@OneToMany(mappedBy = "movie")
	private List<Play> plays = new ArrayList<>();
	
	public Movie() {};
	public Movie(int id, String name, long duration) {
		this.id = id;
		this.name = name;
		this.duration = duration;
	}

	public int getid() {
		return id;
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
