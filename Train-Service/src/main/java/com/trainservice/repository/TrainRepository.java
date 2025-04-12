package com.trainservice.repository;

import java.time.LocalDateTime; 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trainservice.model.Train;

import jakarta.transaction.Transactional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {
	@Query(value = "select t.train_id,train_name, train_source, train_destination,train_departure,train_arrival,class_type,class_seats,class_price from train t join train_class tc on t.train_id=tc.train_id where t.train_source= :source and t.train_destination= :destination and t.train_departure>= :date",nativeQuery = true)
	public List<Object> find(@Param("source") String source,@Param("destination") String destination,@Param("date") LocalDateTime date);

	
	

    @Modifying
    @Transactional
    @Query("UPDATE Train t SET t.trainName = :trainName, t.trainSource = :trainSource, " +
           "t.trainDestination = :trainDestination, t.trainDeparture = :trainDeparture, " +
           "t.trainArrival = :trainArrival WHERE t.trainId = :trainId")
    int updateTrainDetails(@Param("trainId") int trainId, 
                           @Param("trainName") String trainName, 
                           @Param("trainSource") String trainSource, 
                           @Param("trainDestination") String trainDestination,
                           @Param("trainDeparture") LocalDateTime trainDeparture, 
                           @Param("trainArrival") LocalDateTime trainArrival);




}
