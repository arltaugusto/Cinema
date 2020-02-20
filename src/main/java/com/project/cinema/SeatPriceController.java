package com.project.cinema;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.entities.SeatPrice;
import com.project.exceptions.EntityNotFoundException;
import com.project.repositories.SeatPriceRepository;
import com.project.requestobjects.PriceDTO;
import com.project.utils.BasicEntityUtils;

@Controller
@RequestMapping(path="/price") 
public class SeatPriceController {
	
	@Autowired private SeatPriceRepository seatPriceRepository;
	
	@PostMapping(path = "/add")
	public ResponseEntity<String> addNewPrice (@RequestBody PriceDTO price) {
		SeatPrice newSeatPrice = new SeatPrice(price.getActivationDate(), price.getSuperSeatPrice(), price.getRegularSeatPrice());
		seatPriceRepository.save(newSeatPrice);
		return new ResponseEntity<>("added", HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<SeatPrice>> getPrices () {
		return new ResponseEntity<>(seatPriceRepository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/getCurrentPrices")
	public SeatPrice getCurrentPrices () throws EntityNotFoundException {
		Optional<SeatPrice> lastSeatPrice = seatPriceRepository.findAll().stream()
				.filter(seatPrice -> seatPrice.getActivationDate().compareTo(LocalDateTime.now()) < 0)
				.max(Comparator.comparing(SeatPrice::getSetDate));
		return BasicEntityUtils.entityFinder(lastSeatPrice);
	}
}
