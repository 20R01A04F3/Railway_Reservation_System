package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	
	@GetMapping("/health")
	public ResponseEntity<String> check(){
		return ResponseEntity.ok("hello world");
	}

}
