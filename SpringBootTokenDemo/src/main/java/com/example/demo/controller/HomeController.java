package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.JWTRequest;
import com.example.demo.entity.JwtResponse;
import com.example.demo.service.MyUserDetailsService;
import com.example.demo.utils.JwtUtility;

@RestController
public class HomeController {
	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/s")
	public String home() 
	{
		return "Welcome to Postman Home ";
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials", e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

		final String token = jwtUtility.generateToken(userDetails);

		return new JwtResponse(token);
	}

}
