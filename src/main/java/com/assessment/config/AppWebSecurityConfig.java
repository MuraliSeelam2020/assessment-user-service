package com.assessment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.assessment.util.AppJWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private AppAuthenticationProvider authenticationProvider;

	@Autowired
	private AppJWTAuthenticationFilter appJWTAuthenticationFilter;
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
				.and()
			.csrf()
				.disable()
			.authorizeRequests()
				.antMatchers("/api/role/**", "/api/user/assign-role/**", "/api/user/unlock/**").hasAnyAuthority("ROLE_ADMIN")
				.antMatchers("/api/auth/login", "/api/user/signup", 
							 "/v2/api-docs", "/swagger-ui/index.html"
							 )
					.permitAll()
				.anyRequest()
					.authenticated()
			.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(appJWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

}