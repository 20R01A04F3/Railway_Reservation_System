package com.trainservice.controller;

import java.time.LocalDateTime;   
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainservice.dao.TrainDao;
import com.trainservice.dto.ReservationDto;
import com.trainservice.dto.TrainDto;
import com.trainservice.feign.ReservationFeign;
import com.trainservice.model.Train;
import com.trainservice.repository.TrainClassRepository;
import com.trainservice.repository.TrainRepository;
import com.trainservice.service.TrainService;

@RestController
@RequestMapping("/admin")

public class TrainController {
	
	@Autowired
	TrainService ts;
	
	@Autowired
	ReservationFeign reservationFeign;
	
	@Autowired
	TrainRepository trainRepository;
	
	
	@Autowired
	TrainClassRepository trainClassRepository;
	
	@PostMapping("/addtrain")
	public void addTrainDetails(@RequestBody TrainDto td)
	{
		ts.addTrainDetails(td.getTrain(), td.getTrainClass());
	}
	
	@PostMapping("/fetchtrains")
	public List<Object> fetchTrainDetails(@RequestBody Map<String, Object> request) {
	    String source = (String) request.get("source");
	    String destination = (String) request.get("destination");
	    String dateString = (String) request.get("date");
	    LocalDateTime date = LocalDateTime.parse(dateString);
	    return ts.fetchTrainDetails(source, destination, date);
	}
	
	
	
	@GetMapping("/fetchAll")
	public List<Train> fetchAllTrains(){
		return ts.fetchAll();
	}
	
	
	@DeleteMapping("/delete/{id}")
	public Object deleteTrain(@PathVariable int id) {
		  try {
		 
		        if (!trainRepository.existsById(id)) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                                 .body("Train with ID " + id + " not found.");
		        }

		        trainClassRepository.deleteByTrainId(id);
		        trainRepository.deleteById(id);
		       
		        return ResponseEntity.ok("Train with ID " + id + " deleted successfully.");
		    } catch (Exception e) {
		        
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                             .body("An error occurred while deleting the train: " + e.getMessage());
		    }
		
	}
	
	
	@DeleteMapping("deleteTickets/{id}")
	public ResponseEntity<String> deleteTicketsByTrainId(@PathVariable int id){
		return reservationFeign.deleteTicketsByTrainId(id);
	}
	
	
	
	
	
	
	
	@GetMapping("/bookingdetails")
	public ResponseEntity<List<ReservationDto>> bookingDetails()
	
	{
		return reservationFeign.bookingDetails();
	}
	
	@PostMapping("/fetchtrain/{id}")
	public String trainName(@PathVariable Integer id) {
		
		Optional<Train> train = trainRepository.findById(id);
		return train.get().getTrainName();
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<Object> updateTrain(@RequestBody Train train){
		return ResponseEntity.ok(ts.updateTrain(train));
	}
	
}
