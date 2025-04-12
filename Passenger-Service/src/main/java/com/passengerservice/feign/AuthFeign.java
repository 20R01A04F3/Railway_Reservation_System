package com.passengerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.passengerservice.model.User;


@FeignClient(name = "authentication-service",url = "http://localhost:8081")
public interface AuthFeign {
	@GetMapping("/api/auth/getuser")
	public User getUser();

}
