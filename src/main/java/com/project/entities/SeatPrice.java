package com.project.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class SeatPrice {
	
	@Id
	private LocalDateTime setDate;
	
	private LocalDateTime activationDate;
	private float superSeatPrice;
	private float regularSeatPrice;
	
	public SeatPrice(LocalDateTime activationDate, float superSeatPrice, float regularSeatPrice) {
		this.setDate = LocalDateTime.now();
		this.activationDate = activationDate;
		this.superSeatPrice = superSeatPrice;
		this.regularSeatPrice = regularSeatPrice;
	}
	
	public SeatPrice() {}

	public float getSuperSeatPrice() {
		return superSeatPrice;
	}

	public void setSuperSeatPrice(float superSeatPrice) {
		this.superSeatPrice = superSeatPrice;
	}

	public float getRegularSeatPrice() {
		return regularSeatPrice;
	}

	public void setRegularSeatPrice(float regularSeatPrice) {
		this.regularSeatPrice = regularSeatPrice;
	}

	public LocalDateTime getSetDate() {
		return setDate;
	}

	public void setSetDate(LocalDateTime setDate) {
		this.setDate = setDate;
	}

	public LocalDateTime getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(LocalDateTime activationDate) {
		this.activationDate = activationDate;
	}
}
