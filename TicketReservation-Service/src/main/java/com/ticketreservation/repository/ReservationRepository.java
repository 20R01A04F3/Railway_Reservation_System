package com.ticketreservation.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketreservation.model.Reservation;

import jakarta.transaction.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	
	 boolean existsByTrainId(int trainId);
	
	 @Modifying
	    @Transactional
	    @Query("delete from Reservation t where t.trainId = :trainId")
	    void deleteByTrainId(@Param("trainId") int trainId);
	
	@Query(value = "select max(class_price) from train_class clas where clas.train_id= :trainId and class_type= :trainclass",nativeQuery = true)
	public int classPrice(@Param("trainId") int trainId,@Param("trainclass") String trainClass);
	
	
	@Query(value = "select class_seats from train_class clas where clas.train_id= :trainId and class_type= :trainclass",nativeQuery = true)
	public int availableTickets(@Param("trainId") int trainId,@Param("trainclass") String trainClass);
	
	@Query(value = "select * from train_class clas where clas.train_id= :trainId and class_type= :trainclass",nativeQuery = true)
	public Object isClassAvailable(@Param("trainId") int trainId,@Param("trainclass") String trainClass);
	
	
	@Query(value = "select class_id from train_class clas where clas.train_id= :trainId and class_type= :trainclass",nativeQuery = true)
	public int getClassId(@Param("trainId") int trainId,@Param("trainclass") String trainClass);
	
	
	@Modifying
	@Transactional
	@Query(value = "update train_class set class_seats=class_seats- :seats where train_id= :trainId and class_type= :trainclass", nativeQuery = true)
	public void updateSeats(@Param("seats") int seats,@Param("trainId") int trainId,@Param("trainclass") String trainclass);
	
	
	
	
	
	@Query(value = "select r.pnr_number,r.credit_no,p.passenger_name,t.train_name,t.train_source,t.train_destination,tc.class_type,t.train_departure,t.train_arrival,r.reserved_tickets,r.total_price,r.bank_name from reservation r join train_class tc on r.class_id=tc.class_id join train t on t.train_id=tc.train_id join passenger p on r.passenger_id=p.passenger_id where p.passenger_email= :passengerEmail",nativeQuery = true)
	public List<Object[]> reservedTickets(@Param("passengerEmail") String passengerEmail);
	
	
	
	@Modifying
	@Transactional
	@Query(value = "delete from reservation where pnr_number= :pnrNumber", nativeQuery = true)
	public int cancelTicket(@Param("pnrNumber") String pnrNumber);
	
	@Query(value = "Select class_id from train_class where class_id = (Select class_id from reservation where pnr_number= :pnrnumber)",nativeQuery = true)
	public int classInfo(@Param("pnrnumber") String pnrnumber);
	
	public Reservation findBypnrNumber(String pnrNumber);
	
	@Modifying
	@Transactional
	@Query(value = "update train_class set class_seats=class_seats+ :seats where class_id= :classId", nativeQuery = true)
	public void updateCancelledSeats(@Param("seats") int seats,@Param("classId") int classId);
}
