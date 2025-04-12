package com.trainservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainservice.dao.TrainDao;
import com.trainservice.model.Train;
import com.trainservice.model.TrainClass;

@Service
public class TrainService {
	@Autowired
	TrainDao td;
	public void addTrainDetails(Train t,List<TrainClass> tc)
	{
		td.addTrainDetails(t,tc);
	}
	public List<Object> fetchTrainDetails(String source,String destination,LocalDateTime date)
	{
		return td.fetchTrainDetails(source, destination, date);
	}
	
	
	public List<Train> fetchAll(){
		return td.fetchAllTrains();
	}
	
	public int updateTrain(Train train) {
		return td.updateTrain(train);
	}
}
                                             