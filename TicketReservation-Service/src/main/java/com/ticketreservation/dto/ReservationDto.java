package com.ticketreservation.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ReservationDto {
	
	private String pnrNumber;
	private String passengerName; 
	private String trainName;
	private String trainSource;
	private String trainDestination;
	private String trainClass;
	private LocalDateTime trainDeparture;
	private LocalDateTime trainArrival;
	private int noOfTickets;
	private double price;
	
	
	
	
}
