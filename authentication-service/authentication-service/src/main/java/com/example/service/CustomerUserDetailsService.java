package com.example.service;
 
import java.util.Objects;   
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.CustomPassengerDetails;

import com.example.model.Passenger;
import com.example.model.User;
import com.example.repository.PassengerRepository;

 
@Service
public class CustomerUserDetailsService implements UserDetailsService {

//	@Autowired
//	UserRepository repository;
	
	@Autowired
	PassengerRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		User user=repository.findUserByUsername(username);
		Passenger passenger = repository.findBypassengerEmail(email);
		
		if(Objects.isNull(passenger)) {
			throw new UsernameNotFoundException("User Not Found in DB ");
		}
		return new CustomPassengerDetails(passenger);
	}

}
