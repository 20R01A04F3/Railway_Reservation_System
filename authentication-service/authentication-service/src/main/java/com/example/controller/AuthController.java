package com.example.controller;

import com.example.model.AuthRequest;  
import com.example.model.CustomPassengerDetails;
import com.example.model.Passenger;
import com.example.model.Response;
import com.example.model.User;
import com.example.repository.PassengerRepository;

import com.example.service.CustomerUserDetailsService;
import com.example.service.ValidationResponse;

import jakarta.validation.Valid;

import com.example.util.JwtUtil;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
	
	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	
	
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    
//    @Autowired
//    private UserRepository userRepository;
    
    @Autowired
    private PassengerRepository passengerRepository;
    
   
    
   private User user;
    
    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
       
    	
    	

    	Passenger passenger = passengerRepository.findBypassengerEmail(authenticationRequest.getEmail());
    	HashMap<String,String> response = new HashMap<>();
    	if(passenger==null) {
    		response.put("message", "User not found");
    return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    	}
    	if(!passwordEncoder.matches(authenticationRequest.getPassword(), passenger.getPassengerPassword())) {
    		HashMap<String, String> map = new HashMap<>();
    		map.put("message","Login failed Check username and password");
    		map.put("status","401");
    		
    		return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    		
    	};
    	authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
    	
      user = new User(passenger.getPassengerId(),passenger.getPassengerName(),passenger.getPassengerEmail());
        
    	final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
       
     System.out.println("auth controller -------- "+userDetails);
        String token = jwtUtil.generateToken((CustomPassengerDetails)userDetails);
        
        
       logger.info("login completed");
        response.put("message","Login successfull");
       response.put("status","201");
       response.put("token", token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Passenger passenger) {
        if (passengerRepository.findBypassengerEmail(passenger.getPassengerEmail()) != null) {
        	System.out.println("=========>"+passenger);
            return ResponseEntity
                    .badRequest()
                    .body(new String("Error: User email is already in use!"));
        }

        // Encode the password before saving the user
//        String password = encoder.encode(passenger.getPassengerPassword());
        passenger.setPassengerPassword(passwordEncoder.encode(passenger.getPassengerPassword()));
        passenger.setRole("USER");
        passengerRepository.save(passenger);

        return ResponseEntity.ok(new String("User registered successfully!"));
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Authentication Service is up and running");
    }
    


@GetMapping("/validate")
public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
    try {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtUtil.extractUsername(jwtToken);
        
        System.out.println("Inside Validate Token Method");
        UserDetails user = userDetailsService.loadUserByUsername(username);
//      CustomUserDetails cuser=(CustomUserDetails) user;
      CustomPassengerDetails cpassenger =(CustomPassengerDetails) user;

        if (jwtUtil.validateToken(jwtToken, user)) {
            ValidationResponse validationResponse = new ValidationResponse(cpassenger.getEmail(), cpassenger.getPassword(),cpassenger.getRole());
            return ResponseEntity.ok(validationResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    } catch (Exception e) {
      
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}


@GetMapping("/getuser")
public User getUser() {
	return this.user;
}



}