package com.bah.project.authservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bah.project.authservice.domain.Customer;
import com.bah.project.authservice.domain.Token;


@RestController
@RequestMapping("/account")
public class AuthController {

	@Autowired
	private RestTemplate restTemplate;
	
	private static Logger log = LoggerFactory.getLogger(AuthController.class);
	
	
	// WORKS WITH CLIENT
	@PostMapping("/register")
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer){
	
		log.debug("Auth Controller - Register new Customer method - received customer: {}", customer);
		
		restTemplate.postForObject("http://localhost:8080/api/customers", customer, ResponseEntity.class);
		
		return ResponseEntity.ok(customer);
	}
	
	
	@PostMapping("/token")
	public ResponseEntity<Token> getToken(@RequestBody Customer customer){
	
		Token token =  new Token("Im a Token");
		
		return ResponseEntity.ok(token);
	}
	
	
}
