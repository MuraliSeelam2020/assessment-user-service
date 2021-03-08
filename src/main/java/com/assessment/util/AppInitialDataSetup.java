package com.assessment.util;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.assessment.entity.RoleEntity;
import com.assessment.entity.UserEntity;
import com.assessment.entity.UserRoleEntity;
import com.assessment.repository.RoleRepository;
import com.assessment.repository.UserRepository;
import com.assessment.repository.UserRoleRepository;

@Component
public class AppInitialDataSetup implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserRoleRepository userRoleRepo;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		createRoleIfNotFound("ROLE_ADMIN");
		createRoleIfNotFound("ROLE_USER");
		
		createAdminUserIfNotFound("admin");
	}

	@Transactional
	private void createRoleIfNotFound(String name) {
		if (!roleRepo.findByName(name).isPresent()) {
			roleRepo.save(new RoleEntity(null, name, true));
		}
	}
	
	@Transactional
	private void createAdminUserIfNotFound(String name) {
		if (!userRepo.findByUserName(name).isPresent()) {
			UserEntity userEntity = userRepo.save(new UserEntity(null, name, name, name, name, bcryptEncoder.encode(name), true, 0, false));			
			
			roleRepo.findAll().stream()
				.filter(r -> r.isActive())
				.forEach(roleEntity -> userRoleRepo.saveAndFlush(new UserRoleEntity(null, userEntity, roleEntity)));	
		}
	}

}