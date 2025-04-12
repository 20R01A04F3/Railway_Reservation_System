package com.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
	private int passengerId;
	private String trainClass;
	private String address;
	private String bankName;
	private String creditNo;
	private int reservedTickets;
	private int trainId;
	
}
