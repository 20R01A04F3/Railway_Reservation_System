package com.ticketreservation.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketreservation.model.Reservation;
import com.ticketreservation.model.ResponseData;
import com.ticketreservation.model.TicketInfo;
import com.ticketreservation.repository.ReservationRepository;
import com.ticketreservation.service.ReservationService;

@RestController
public class ReservationController {
	
	@Autowired
	ReservationService reserveService;
	
	@Autowired
	ReservationRepository reservationRepo;
	
	
	
	
	@GetMapping("/view-ticket/{pnr}")
	public Reservation viewTikcet(@PathVariable String pnr) {
		return reserveService.viewTicket(pnr);
	}
	
	
	@PostMapping("/ticketreservation")
	public String reserveTickets(@RequestBody Reservation reserve)
	{
		return reserveService.reserveTickets(reserve);
	}
	
	@GetMapping("/reservedtickets")
	public ResponseEntity<List<Object[]>> reservedTickets(@RequestParam String passengerEmail)
	{
		
		return new ResponseEntity<>(reserveService.reservedTickets(passengerEmail),HttpStatus.OK);
	}
	
	@DeleteMapping("/cancelticket")
	public ResponseEntity<String> cancelTicket(@RequestParam String pnrNumber)
	{
		return ResponseEntity.ok(reserveService.cancelTicket(pnrNumber));
	}
	
	@GetMapping("/bookingdetails")
	public ResponseEntity<List<ResponseData>> bookingDetails()
	{
		return ResponseEntity.ok(reserveService.bookingDetails());
	}
	
	@PostMapping("/extractpassenger")
	public void extractPassenger(@RequestBody Integer id)
	{
		reserveService.extractPassenger(id);
	}
	
	@DeleteMapping("deleteTickets/{id}")
	public ResponseEntity<String> deleteTicketsByTrainId(@PathVariable int id) {
	    if (!reservationRepo.existsByTrainId(id)) { // Check if trainId exists
	        return ResponseEntity.ok(id+"not available");
	    }

	    reservationRepo.deleteByTrainId(id);
	    return ResponseEntity.ok("Tickets with train ID " + id + " deleted successfully.");
	}
	
	
	@PostMapping("/getTickets")
	public ResponseEntity<Object> getNumberOfSeats(@RequestBody TicketInfo info){
		
		HashMap <String ,Integer> map = new HashMap<>();
		
		int seats =reservationRepo.availableTickets(info.getTrainId(),info.getTrainClass());
		if(seats==0) {
			map.put("seats", 0);
		}
		map.put("seats", seats);
		
		return ResponseEntity.ok(map);
		
		
	}
	
	
	
}
