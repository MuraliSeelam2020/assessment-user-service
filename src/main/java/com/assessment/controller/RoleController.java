package com.assessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.dto.RoleDTO;
import com.assessment.service.RoleService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping
	@ApiOperation(value = "This method is used to add new role.")
	public String createRole(@RequestBody RoleDTO roleDTO) {
		return roleService.createRole(roleDTO);
	}

	@GetMapping
	@ApiOperation(value = "This method is used to get all roles.")
	public List<RoleDTO> getAllRoles() {
		return roleService.getAllRoles();
	}
	
	@GetMapping("/{roleId}")
	@ApiOperation(value = "This method is used to get role data by id.")
	public RoleDTO getRole(@PathVariable Long roleId) {
		return roleService.getRole(roleId);
	}
	
	@DeleteMapping("/{roleId}")
	@ApiOperation(value = "This method is used to inactivate role.")
	public String deleteRole(@PathVariable Long roleId) {
		return roleService.deleteRole(roleId);
	}
	
	@PutMapping("/{roleId}")
	@ApiOperation(value = "This method is used to activate role.")
	public String activateRole(@PathVariable Long roleId) {
		return roleService.activateRole(roleId);
	}
	
}
