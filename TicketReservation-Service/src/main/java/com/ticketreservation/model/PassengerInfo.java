package com.ticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PassengerInfo {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int passenger_id;
	private String passenger_name;
	private String passenger_gender;
	private int passenger_age;

}
