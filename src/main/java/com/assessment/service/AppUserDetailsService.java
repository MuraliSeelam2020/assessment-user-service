package com.assessment.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.assessment.entity.UserEntity;
import com.assessment.entity.UserRoleEntity;
import com.assessment.exception.AppException;
import com.assessment.repository.UserRepository;
import com.assessment.repository.UserRoleRepository;

@Service("userDetailsService")
@Transactional
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUserName(userName).orElseThrow(() -> new AppException("User name not found"));
		try {
			return new User(user.getUserName(), user.getPassword(), user.isActive(), true, true, 
					user.getWrongPasswordEntries() < 3, getAuthorities(userRoleRepository.findByUser(user)));			
		} catch (BadCredentialsException bde) {
			int wrongPasswordEntries = user.getWrongPasswordEntries() + 1;
			user.setWrongPasswordEntries(wrongPasswordEntries);
			if (wrongPasswordEntries > 2) {
				user.setLocked(true);	
			}
			userRepository.save(user);
			
			throw bde;
		} catch (Exception e) {
			int wrongPasswordEntries = user.getWrongPasswordEntries() + 1;
			user.setWrongPasswordEntries(wrongPasswordEntries);
			if (wrongPasswordEntries > 2) {
				user.setLocked(true);	
			}
			userRepository.save(user);
			
			throw e;
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(List<UserRoleEntity> userRoles) {
		return getGrantedAuthorities(userRoles);
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<UserRoleEntity> userRoles) {
		return userRoles.stream().map(r -> new SimpleGrantedAuthority(r.getRole().getName())).collect(Collectors.toList());
	}
}