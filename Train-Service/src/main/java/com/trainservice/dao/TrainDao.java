package com.trainservice.dao;

import java.time.LocalDateTime; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainservice.model.Train;
import com.trainservice.model.TrainClass;
import com.trainservice.repository.TrainClassRepository;
import com.trainservice.repository.TrainRepository;

@Service
public class TrainDao {
	
	@Autowired
	TrainRepository tr;
	@Autowired
	TrainClassRepository tcr;
	public void addTrainDetails(Train t,List<TrainClass> tc)
	{
		for(TrainClass obj: tc)
			obj.setTrain(t);
		tr.save(t);
		tcr.saveAll(tc);
	}
	public List<Object> fetchTrainDetails(String source,String destination,LocalDateTime date)
	{
		return tr.find(source, destination, date);
	}
	
	public List<Train> fetchAllTrains(){
		return tr.findAll();
	}
	
	public int updateTrain(Train train) {
		Train trainFind = tr.findById(train.getTrainId()).orElse(null); 
		
		if(trainFind!=null) {
			tr.updateTrainDetails(train.getTrainId(),train.getTrainName(),train.getTrainSource(),train.getTrainDestination(),train.getTrainDeparture(),train.getTrainArrival());
			return 1;
			
		}
		else return -1;	
	}
	
}
