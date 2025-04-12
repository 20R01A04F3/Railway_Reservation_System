package com.passengerservice.controller;

import java.util.List;    
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.passengerservice.dto.ReservationDto;
import com.passengerservice.feign.AuthFeign;
import com.passengerservice.feign.PaymentFeign;
import com.passengerservice.feign.ReservationFeign;
import com.passengerservice.feign.TrainFeign;
import com.passengerservice.model.User;
import com.passengerservice.payment.ProductRequest;
 

@RestController
@RequestMapping("/passenger")
public class PassengerController {
		
	@Autowired
	TrainFeign trainFeign;
	
	@Autowired
	ReservationFeign rerservationFeign;
	 
//	@Autowired
//	PassengerService passengerService;
	
	@Autowired  
	PaymentFeign paymentFeign;
	
	@Autowired
	AuthFeign authFeign;
	
	@PostMapping("/fetchtrains")
	public List<Object> fetchTrainDetails(@RequestBody Map<String, Object> request)
	{

		return trainFeign.fetchTrainDetails(request);
	}
	
	
	@PostMapping("/ticketreservation")
	public ResponseEntity<String> reserveTickets(@RequestBody ReservationDto reserve)
	{
		User user = authFeign.getUser();
		reserve.setPassengerId(user.getPassengerId());
		return new ResponseEntity<String>(rerservationFeign.reserveTickets(reserve),HttpStatus.OK);
	}
	
	@GetMapping("/reservedtickets")
	public ResponseEntity<List<Object[]>> reservedTickets()
	{
		User user = authFeign.getUser();
		
		return rerservationFeign.reservedTickets(user.getPassengerEmail());
	}
	
	@DeleteMapping("/cancelticket")
	public ResponseEntity<String> cancelTicket(@RequestParam String pnrNumber)
	{
		return rerservationFeign.cancelTicket(pnrNumber);
	}
	
	@PostMapping("/make-payment")
	public Object makePayment(@RequestBody  ProductRequest request){
		if (request.getAmount() == null) {
	        return new ResponseEntity<>("Amount cannot be null", HttpStatus.BAD_REQUEST);
	    }
		return paymentFeign.checkoutProducts(request);
	}
	
	
	@GetMapping("/view-ticket/{pnr}")
	public ResponseEntity<Object> viewTicket(@PathVariable String pnr) {
		return  ResponseEntity.ok(rerservationFeign.viewTicket(pnr));
	}
	
	
	@PostMapping("/getTickets")
	public ResponseEntity<Object> getNoOfTickets(@RequestBody Object obj){
		return rerservationFeign.getNumberOfSeats(obj);
	}
	
	
}
