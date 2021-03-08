package com.assessment.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.dto.RoleDTO;
import com.assessment.entity.RoleEntity;
import com.assessment.exception.AppException;
import com.assessment.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepo;

	@Transactional(rollbackOn = Exception.class)
	public String createRole(RoleDTO roleDTO) {
		if (!roleRepo.findByName(roleDTO.getName()).isPresent()) {
			roleRepo.save(new RoleEntity(null, roleDTO.getName(), true));
		} else {
			return "Role already exists";
		}
		return "Role created successfully";
	}

	@Transactional(rollbackOn = Exception.class)
	public List<RoleDTO> getAllRoles() {
		return roleRepo.findAll().stream()
				.filter(r -> r.isActive())
				.map(r -> r.toRoleDTO())
				.collect(Collectors.toList());
	}

	@Transactional(rollbackOn = Exception.class)
	public RoleDTO getRole(Long roleId) {
		return roleRepo.findById(roleId).orElseThrow(() -> new AppException("Role not found")).toRoleDTO();
	}

	@Transactional(rollbackOn = Exception.class)
	public String deleteRole(Long roleId) {
		RoleEntity roleEntity = roleRepo.findById(roleId).orElseThrow(() -> new AppException("Role not found"));
		roleEntity.setActive(false);
		roleRepo.save(roleEntity);
		
		return "Role deleted successfully";
	}

	@Transactional(rollbackOn = Exception.class)
	public String activateRole(Long roleId) {
		RoleEntity roleEntity = roleRepo.findById(roleId).orElseThrow(() -> new AppException("Role not found"));
		roleEntity.setActive(true);
		roleRepo.save(roleEntity);
		
		return "Role activated successfully";
	}
	
}
