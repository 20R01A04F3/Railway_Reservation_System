package com.trainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trainservice.model.TrainClass;

import jakarta.transaction.Transactional;


@Repository
public interface TrainClassRepository extends JpaRepository<TrainClass, Integer> {
	   @Modifying
	    @Transactional
	    @Query("DELETE FROM TrainClass t WHERE t.train.trainId = :trainId")
	    void deleteByTrainId(@Param("trainId") int trainId);
}
