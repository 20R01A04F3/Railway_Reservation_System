package com.passengerservice.feign;

import java.util.List;  

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.passengerservice.dto.ReservationDto;


@FeignClient(name = "ticketreservation-service", url = "http://localhost:8084")
public interface ReservationFeign {
	
	@PostMapping("/ticketreservation")
	public String reserveTickets(@RequestBody ReservationDto reserve);
	
	@GetMapping("/reservedtickets")
	public ResponseEntity<List<Object[]>> reservedTickets(@RequestParam String passengerEmail);
	
	@DeleteMapping("/cancelticket")
	public ResponseEntity<String> cancelTicket(@RequestParam String pnrNumber);
	
	@GetMapping("/view-ticket/{pnr}")
	public Object viewTicket(@PathVariable String pnr);
	
	
	@PostMapping("/getTickets")
	public ResponseEntity<Object> getNumberOfSeats(@RequestBody Object obj);
}
