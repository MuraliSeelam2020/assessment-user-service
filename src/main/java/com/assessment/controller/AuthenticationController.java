package com.assessment.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.exception.AppException;
import com.assessment.service.AuthenticationService;

import io.swagger.annotations.ApiOperation;
import lombok.Data;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Data
	public static class LoginDTO {
		private String userName;
		private String password;
	}

	@PostMapping("/login")
	@ApiOperation(value = "This method is used to login user, require username and password")
	public String login(@RequestBody LoginDTO loginDTO, HttpServletResponse httpResponse) {
		String token = authenticationService.authenticate(loginDTO);
		setCookieForLogin(token, httpResponse);
		
		return "Login successful";
	}

	@PostMapping("/logout")
	@ApiOperation(value = "This method is used to logout user")
	public String logout(HttpServletResponse httpResponse) {
		setCookieForLogout(httpResponse);
		return "Logout successful";
	}

    private void setCookieForLogin(String token, HttpServletResponse httpResponse) {
        try {
	        if (token != null) {
	            Cookie cookie = new Cookie("platform-auth", token);
	            cookie.setPath("/");
	            cookie.setHttpOnly(true);
	            httpResponse.addCookie(cookie);
	        }
        } catch(Exception e) {
        	throw new AppException("Login failed");
        }
    }
    
    private void setCookieForLogout(HttpServletResponse httpResponse) {
    	try {
	    	Cookie cookie = new Cookie("platform-auth-delete", null);
			cookie.setPath("/");
            cookie.setHttpOnly(true);
            httpResponse.addCookie(cookie);
    	}catch(Exception e) {
    		throw new AppException("Login failed");
    	}
    }
	
}