package com.assessment.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.assessment.entity.UserEntity;
import com.assessment.exception.AppException;
import com.assessment.repository.UserRepository;
import com.assessment.repository.UserRoleRepository;

@Component
public class AppAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private BCryptPasswordEncoder bCrypt;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		UserEntity user = userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new AppException("User name not found"));
		if (user.isLocked()) {
			throw new AppException("User is locked");
		}

		boolean matches = bCrypt.matches(authentication.getCredentials().toString(), user.getPassword());
		if (matches) {
			if (user.getWrongPasswordEntries() > 0) {
				user.setWrongPasswordEntries(0);
				userRepository.save(user);
			}

			return new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword(), getAuthorities(user));
		}

		int wrongPasswordEntries = user.getWrongPasswordEntries() + 1;
		user.setWrongPasswordEntries(wrongPasswordEntries);
		if (wrongPasswordEntries > 2) {
			user.setLocked(true);
		}
		userRepository.save(user);

		throw new AppException("Invalid password");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(UserEntity user) {
		return userRoleRepository.findByUser(user).stream().map(r -> new SimpleGrantedAuthority(r.getRole().getName())).collect(Collectors.toList());
	}

}