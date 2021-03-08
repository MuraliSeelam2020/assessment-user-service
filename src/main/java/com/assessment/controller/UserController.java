package com.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.dto.SignupDTO;
import com.assessment.dto.UserDTO;
import com.assessment.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	@ApiOperation(value = "This method is used to signup users.")
	public String create(@RequestBody SignupDTO signupDTO) {
		return userService.createUser(signupDTO);
	}

	@GetMapping("/info/{userId}")
	@ApiOperation(value = "This method is used to get user data.")
	public UserDTO getUser(@PathVariable Long userId) {
		return userService.getUser(userId);
	}
	
	@PutMapping("/assign-role/{userId}/{roleId}")
	@ApiOperation(value = "This method is used to assign roles to users.")
	public String assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
		return userService.assignRole(userId, roleId);
	}

	@PutMapping("/unlock/{userId}")
	@ApiOperation(value = "This method is used to unlock users.")
	public String unlock(@PathVariable Long userId) {
		return userService.unlockUser(userId);
	}
}
