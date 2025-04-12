package com.trainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
	private int id;
	private String pnr;
	private int tickets;
	private String trainName;
	private String className;
}
