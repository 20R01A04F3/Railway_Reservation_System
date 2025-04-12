package com.passengerservice.model;

import jakarta.persistence.Column; 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Passenger {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int passengerId;
	
	@Column(nullable = false)
	private String passengerName;
	@Column(nullable = false)
	private int passengerAge;
	@Column(nullable = false)
	private String passengerContactNo;
	@Column(nullable = false)
	private String passengerEmail;
	@Column(nullable = false)
	private String passengerPassword;
	@Column(nullable = false)
	private String role;
}
