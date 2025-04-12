package com.ticketreservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
	
	private int id;
	private String pnr;
	private int tickets;
	private String trainName;
	private String className;

}
