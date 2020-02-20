package com.project.requestobjects;

import java.time.LocalDateTime;

public class PriceDTO {
	
	private LocalDateTime activationDate;
	private float regularSeatPrice;
	private float superSeatPrice;
	
	public PriceDTO() {}
	
	public PriceDTO(float regularSeatPrice, float superSeatPrice, LocalDateTime activationDate) {
		this.activationDate = activationDate;
		this.regularSeatPrice = regularSeatPrice;
		this.superSeatPrice = superSeatPrice;
	}
	
	public float getRegularSeatPrice() {
		return regularSeatPrice;
	}
	
	public void setRegularSeatPrice(float regularSeatPrice) {
		this.regularSeatPrice = regularSeatPrice;
	}
	
	public float getSuperSeatPrice() {
		return superSeatPrice;
	}
	
	public void setSuperSeatPrice(float superSeatPrice) {
		this.superSeatPrice = superSeatPrice;
	}

	public LocalDateTime getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(LocalDateTime activationDate) {
		this.activationDate = activationDate;
	}
	
	
}
