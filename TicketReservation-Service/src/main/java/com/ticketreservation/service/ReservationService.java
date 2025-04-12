package com.ticketreservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketreservation.dao.ReservationDao;
import com.ticketreservation.model.Reservation;
import com.ticketreservation.model.ResponseData;

@Service
public class ReservationService {
	
	@Autowired
	ReservationDao reserveDao;
	
	
	public String reserveTickets(Reservation reserve)
	{
		return reserveDao.reserveTickets(reserve);
	}
	
	public List<Object[]> reservedTickets(String passengerEmail)
	{
		return reserveDao.reservedTickets(passengerEmail);
	}
	
	public String cancelTicket(String pnrNumber)
	{
		return reserveDao.cancelTicket(pnrNumber);
	}
	
	public List<ResponseData> bookingDetails()
	{
		return reserveDao.bookingDetails();
	}
	
	public void extractPassenger(int id)
	{
		reserveDao.extractPassenger(id);
	}
	
	public Reservation viewTicket(String pnrNumber) {
		 return reserveDao.viewTicket(pnrNumber);
		
	}
}
