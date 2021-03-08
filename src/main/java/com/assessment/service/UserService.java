package com.assessment.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.assessment.dto.SignupDTO;
import com.assessment.dto.UserDTO;
import com.assessment.entity.RoleEntity;
import com.assessment.entity.UserEntity;
import com.assessment.entity.UserRoleEntity;
import com.assessment.exception.AppException;
import com.assessment.repository.RoleRepository;
import com.assessment.repository.UserRepository;
import com.assessment.repository.UserRoleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Transactional(rollbackOn = Exception.class)
	public String createUser(SignupDTO signupDTO) {
		try {
			if (userRepository.findByUserName(signupDTO.getUserName()).isPresent()) {
				return "User name already exists";
			}

			UserEntity userEntity = userRepository
					.save(new UserEntity(null, signupDTO.getUserName(), signupDTO.getFirstName(), signupDTO.getLastName(),
							signupDTO.getEmail(), bcryptEncoder.encode(signupDTO.getPassword()), true, 0, false));

			userRoleRepository.save(new UserRoleEntity(null, userEntity, roleRepository.findByName("ROLE_USER").get()));

			return "User signup successful";
		} catch (Exception e) {
			log.error("User signup failed", e);
			throw new AppException("User signup failed");
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public UserDTO getUser(Long userId) {
		try {
			UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new AppException("User not found"));			
			List<UserRoleEntity> userRoles = userRoleRepository.findByUser(userEntity);

			UserDTO userDTO = userEntity.toUserDTO();
			userDTO.setRoles(userRoles.stream().map(u -> u.getRole().getName()).collect(Collectors.toList()));
			
			return userDTO;
		} catch (Exception e) {
			log.error("User details get failed", e);
			throw new AppException("User details get failed");
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public String assignRole(Long userId, Long roleId) {
		try {
			UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new AppException("User not found"));
			RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow(() -> new AppException("Role not found"));
			
			List<UserRoleEntity> userRoles = userRoleRepository.findByUserAndRole(userEntity, roleEntity);

			if (userRoles.isEmpty()) {
				userRoleRepository.save(new UserRoleEntity(null, userEntity, roleEntity));	
			}

			return "Role assigned successful";
		} catch (Exception e) {
			log.error("Role assign failed", e);
			throw new AppException("Role assign failed");
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public String unlockUser(Long userId) {
		try {
			UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new AppException("User not found"));
			userEntity.setLocked(false);
			userEntity.setPassword(bcryptEncoder.encode("password"));
			userEntity.setWrongPasswordEntries(0);
			
			userRepository.save(userEntity);
			return "User unlocked successful";
		} catch (Exception e) {
			log.error("User unlock failed", e);
			throw new AppException("User unlock failed");
		}
	}
	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUserName(userName).orElseThrow(() -> new AppException("User name not found"));
		try {
			return new User(user.getUserName(), user.getPassword(), user.isActive(), true, true, user.isLocked(),
					userRoleRepository.findByUser(user).stream().map(r -> 
						new SimpleGrantedAuthority(r.getRole().getName())).collect(Collectors.toList()));
		} catch (Exception e) {
			log.error("User validation failed", e);
			throw new AppException("User validation failed");
		}
	}
	
}
