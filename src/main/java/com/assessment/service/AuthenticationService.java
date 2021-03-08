package com.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.assessment.controller.AuthenticationController.LoginDTO;
import com.assessment.util.AppJWTTokenProvider;

@Service
public class AuthenticationService {

	@Autowired
    private AuthenticationProvider authenticationProvider;

	@Autowired
	private AppJWTTokenProvider appJWTTokenProvider;
	
	public String authenticate(LoginDTO loginDTO) {
		final Authentication authentication = authenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return appJWTTokenProvider.generateToken(authentication);
	}
	
}
