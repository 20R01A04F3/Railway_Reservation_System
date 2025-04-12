package com.trainservice.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.trainservice.dto.ReservationDto;



@FeignClient(name = "ticketreservation-service",url = "http://localhost:8084")

public interface ReservationFeign {
	@GetMapping("/bookingdetails")
	public ResponseEntity<List<ReservationDto>> bookingDetails();
	
	
	@DeleteMapping("deleteTickets/{id}")
	public ResponseEntity<String> deleteTicketsByTrainId(@PathVariable int id);

}



