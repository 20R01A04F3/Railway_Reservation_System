package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Passenger;

public interface PassengerRepository  extends JpaRepository<Passenger, Integer>{
	public Passenger findBypassengerEmail(String pEmail);

}
