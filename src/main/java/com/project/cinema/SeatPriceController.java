package com.project.cinema;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.entities.SeatPrice;
import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.PricesAlreadyActivatedException;
import com.project.repositories.SeatPriceRepository;
import com.project.requestobjects.PriceDTO;
import com.project.utils.BasicEntityUtils;

@RestController
@CrossOrigin
@RequestMapping(path="/price") 
public class SeatPriceController {
	
	@Autowired private SeatPriceRepository seatPriceRepository;
	
	@PostMapping(path = "/add", consumes = "application/json")
	public ResponseEntity<String> addNewPrice (@RequestBody PriceDTO price) {
		SeatPrice newSeatPrice = new SeatPrice(price.getActivationDate(), price.getSuperSeatPrice(), price.getRegularSeatPrice());
		seatPriceRepository.save(newSeatPrice);
		return new ResponseEntity<>("added", HttpStatus.OK);
	}
	
	
	@GetMapping(path="/getAll")
	public ResponseEntity<List<SeatPrice>> getPrices () {
		return new ResponseEntity<>(seatPriceRepository.findAll(), HttpStatus.OK);
	}
	
	@PostMapping(path = "/delete", consumes = "application/json") 
	public void deletePrices (@RequestBody PriceDTO price) throws PricesAlreadyActivatedException, EntityNotFoundException {
		//List<SeatPrice> listita = seatPriceRepository.findAll();  
		SeatPrice pri = BasicEntityUtils.entityFinder(seatPriceRepository.findById(price.getSetDate())); 
		checkActivationDate(pri);
		seatPriceRepository.delete(pri);
	}

	private void checkActivationDate(SeatPrice price) throws PricesAlreadyActivatedException {
		if(price.getActivationDate().isBefore(LocalDateTime.now())) {
			throw new PricesAlreadyActivatedException("These prices have already been activated and cannot be deleted.");
		}
	}
	
	
	@GetMapping(path = "/getCurrentPrices")
	public ResponseEntity<SeatPrice> getCurrentPrices () throws EntityNotFoundException {
		Optional<SeatPrice> lastSeatPrice = seatPriceRepository.findAll().stream()
			.filter(seatPrice -> seatPrice.getActivationDate().compareTo(LocalDateTime.now()) < 0)
			.max(Comparator.comparing(SeatPrice::getActivationDate).thenComparing(SeatPrice::getSetDate));
		return new ResponseEntity<>(BasicEntityUtils.entityFinder(lastSeatPrice), HttpStatus.OK);
	}
}
