package com.ticketreservation.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "train-service", url = "http://localhost:8083")
public interface TrainFeign {
	@PostMapping("/fetchtrains")
	public List<Object> fetchTrainDetails(@RequestBody Map<String, Object> request);
	
	@PostMapping("/admin/fetchtrain/{id}")
	public String trainName(@PathVariable Integer id);

}
