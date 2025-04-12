package com.passengerservice.feign;

import java.util.List; 
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "train-service", url = "http://localhost:8083")
public interface TrainFeign {
	

	
	@PostMapping("/admin/fetchtrains")
	public List<Object> fetchTrainDetails(@RequestBody Map<String, Object> request);
}
