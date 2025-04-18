package com.passengerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.passengerservice.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
	
	public Passenger findBypassengerEmail(String pEmail);
}
