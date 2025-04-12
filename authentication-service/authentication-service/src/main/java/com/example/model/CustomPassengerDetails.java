package com.example.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomPassengerDetails  implements UserDetails {
	
	private Passenger passenger;
	
	
	public CustomPassengerDetails() {
		super();
	}
	
	public CustomPassengerDetails(Passenger passenger) {
		this.passenger = passenger;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		
		return Arrays.stream(passenger.getRole().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
	}

	@Override
	public String getPassword() {
	
		
		return passenger.getPassengerPassword();
	}

	@Override
	public String getUsername() {
		
		
		return passenger.getPassengerEmail();
	}
	
	
	public String getUser() {
		return passenger.getPassengerName();
	}
	
	public String getEmail() {
		return passenger.getPassengerEmail();
	}
	

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {
	
		return passenger.isActive();
	}

	public String getRole() {
		return passenger.getRole();
	}
	


}


