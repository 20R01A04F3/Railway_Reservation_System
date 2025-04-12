package com.ticketreservation.dao;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketreservation.feign.TrainFeign;
import com.ticketreservation.model.Reservation;
import com.ticketreservation.model.ResponseData;
import com.ticketreservation.repository.ReservationRepository;

@Service
public class ReservationDao {
	
	@Autowired
	ReservationRepository reserveRepo;
	
	@Autowired
	TrainFeign trainFeign;
	
	int pId;
	
	public void extractPassenger(int id)
	{
		pId=id;
	}
	
	public String reserveTickets(Reservation reserve)
	{
		if(reserve.getReservedTickets()>6)
			return "Maximum of only 6 tickets can be reserved in any class";
		
		
		if(reserveRepo.isClassAvailable(reserve.getTrainId(), reserve.getTrainClass())==null)
			return "There are no train class of name: "+reserve.getTrainClass()+" or Train Id: "+reserve.getTrainId();
		
		
		if(reserveRepo.availableTickets(reserve.getTrainId(), reserve.getTrainClass())>=reserve.getReservedTickets())
		{
		//PNR Generation
		Random ran= new Random();
		int pnrSeven=1000000+ran.nextInt(9000000);
		reserve.setPnrNumber(reserve.getTrainClass().substring(0, 3).toUpperCase()+pnrSeven);
		
		//ClassId Upadtion
		reserve.setClassId(reserveRepo.getClassId(reserve.getTrainId(), reserve.getTrainClass()));
		
		//fare updation
		int price=reserveRepo.classPrice(reserve.getTrainId(), reserve.getTrainClass());
		reserve.setTotalPrice(price*reserve.getReservedTickets());
		
//		reserve.setPassengerId(pId);
		Reservation status=reserveRepo.save(reserve);
		reserveRepo.updateSeats(reserve.getReservedTickets(), reserve.getTrainId(), reserve.getTrainClass());
		
		if(status!=null)
			return "Tickets reserved successfully your PNR number is: "+status.getPnrNumber();
		return "Not able to reserve the ticket";
		}
		else
		{
			return "Not able to reserve the tickets only "+reserveRepo.availableTickets(reserve.getTrainId(), reserve.getTrainClass())+" tickets are available";
		}
		
	}
	
	public List<Object[]> reservedTickets(String passengerEmail)
	{
		return reserveRepo.reservedTickets(passengerEmail);
	}
	
	public String cancelTicket(String pnrNumber)
	{
		try {
		int classId = reserveRepo.classInfo(pnrNumber);
		 int cancelledSeats = reserveRepo.findBypnrNumber(pnrNumber).getReservedTickets();
		 reserveRepo.updateCancelledSeats(cancelledSeats, classId);
		 int res=reserveRepo.cancelTicket(pnrNumber);
		 if(res>0)
			 return "Ticket Cancelled";
		}
		catch (Exception e) {
			// TODO: handle exception
			return "PNR Number Does not exist";
		}
			
		
		return "Ticket cannot be cancelled";
	}
	
	public List<ResponseData> bookingDetails()
	{
		 // Fetch all reservations from the repository
	    List<Reservation> reservations = reserveRepo.findAll();

	    // Convert the List<Reservation> to List<ResponseData>
	    List<ResponseData> responseList = reservations.stream().map(reservation -> {
	        ResponseData responseData = new ResponseData();
	        responseData.setId(reservation.getReservationId());  
	        responseData.setPnr(reservation.getPnrNumber());  
	        responseData.setTickets(reservation.getReservedTickets());  
	        responseData.setTrainName(trainFeign.trainName(reservation.getTrainId()));  
	        responseData.setClassName(reservation.getTrainClass());  
	         return responseData;
	    }).collect(Collectors.toList());  // Collect the result into a List<ResponseData>

	    return responseList;
	}
	
	
	public Reservation viewTicket(String pnrNumber) {
		
		return reserveRepo.findBypnrNumber(pnrNumber);
	}
}
