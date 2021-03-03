package com.bah.project.authservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	
	
	// to support dockerization
	private final String apiHost = System.getenv("API_HOST"); 
	
		
	@PostMapping("/register")
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer){
	
		log.debug("Auth Controller - Register new Customer method - received customer: {}", customer);
		
		
		//Create APIToken to call DataService
		Token apiToken = jwtUtil.createToken("API_TOKEN");
		log.debug("Auth Controller - API_TOKEN generate: {}", apiToken.getToken());

		// Add Token to Header
		HttpHeaders headers = new HttpHeaders();
		headers.set("authorization", "Bearer " + apiToken.getToken().toString());
		HttpEntity<?> entity = new HttpEntity<>(customer, headers);
		
		
		restTemplate.postForObject(apiHost + "/api/customers", entity, ResponseEntity.class);
		
		return ResponseEntity.ok(customer);
	}
	
	
	@PostMapping("/token")
	public ResponseEntity<?> getToken(@RequestBody Customer customer){

		String username = customer.getName();
		
		//Create APIToken to call DataService
		Token apiToken = jwtUtil.createToken("API_TOKEN");
		log.debug("Auth Controller - API_TOKEN generate: {}", apiToken.getToken());

		// Add Token to Header
		HttpHeaders headers = new HttpHeaders();
		headers.set("authorization", "Bearer " + apiToken.getToken().toString());
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Customer> customerDetailsResponse = restTemplate.exchange(apiHost + "/customers/byname/{username}", HttpMethod.GET, entity, Customer.class, username);
		
		Customer customerDetails = customerDetailsResponse.getBody();
		
		// log.debug("Auth Controller - Get Token for Customer method - received customerDetails: {}", customerDetails);
		
		// if in DB create and return token
		if(customer.getName().equals(customerDetails.getName()) && customer.getPassword().equals(customerDetails.getPassword())) {

			String scopes = "scope: [\"AUTH\"], scope:[\"com.api.customer.r\"]"; 
			Token token = jwtUtil.createToken(scopes); 
			log.debug("Auth Controller - Returning Token - token: {}", token);
			return ResponseEntity.ok(token);
		}
		// otherwise return UNAUTHORIZED
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
