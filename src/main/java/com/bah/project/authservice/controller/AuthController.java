package com.bah.project.authservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bah.project.authservice.domain.Customer;
import com.bah.project.authservice.domain.Token;
import com.bah.project.authservice.service.JwtUtil;


@RestController
@RequestMapping("/account")
public class AuthController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private static Logger log = LoggerFactory.getLogger(AuthController.class);
	
	
	// WORKS WITH CLIENT
	
	@PostMapping("/register")
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer){
	
		log.debug("Auth Controller - Register new Customer method - received customer: {}", customer);
		
		restTemplate.postForObject("http://localhost:8080/api/customers", customer, ResponseEntity.class);
		
		return ResponseEntity.ok(customer);
	}
	
	
	// WORKS WITH CLIENT
	
	@PostMapping("/token")
	public ResponseEntity<?> getToken(@RequestBody Customer customer){
	
		// ADD LOGIC TO VERIFY CUSTOMER IN DB
		
		log.debug("Auth Controller - Get Token for Customer method - received customer: {}", customer);
		log.debug("Auth Controller - Get Token for Customer method - received name: {}", customer.getName());
		log.debug("Auth Controller - Get Token for Customer method - received password: {}", customer.getPassword());
		
		// check for customer in db
		String username = customer.getName();
		Customer customerDetails = restTemplate.postForObject("http://localhost:8080/api/customers/byname", username, Customer.class);
		
		log.debug("Auth Controller - Get Token for Customer method - received customerDetails: {}", customerDetails);
		
		// if in DB create and return token
		if(customer.getName().equals(customerDetails.getName()) && customer.getPassword().equals(customerDetails.getPassword())) {

			// TODO set scopes (Causing failed login)
			
			// String scopes = "ALL";
			String scopes = null;
			Token token = jwtUtil.createToken(scopes); 
			
			return ResponseEntity.ok(token);

			
		}
		// otherwise return UNAUTHORIZED
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	
}
