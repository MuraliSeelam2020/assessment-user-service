package com.assessment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assessment.entity.RoleEntity;
import com.assessment.entity.UserEntity;
import com.assessment.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

	List<UserRoleEntity> findByUser(UserEntity user);
	
	List<UserRoleEntity> findByUserAndRole(UserEntity user, RoleEntity role);
	
}
