package com.assessment.util;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assessment.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppJWTAuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.header.string}")
	public String headerValue;

	@Value("${jwt.token.prefix}")
	public String tokenPrefix;

	@Autowired
	private UserService userService;

	@Autowired
	private AppJWTTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		getLoginUser(req);
		chain.doFilter(req, res);
	}
	
    private void getLoginUser(HttpServletRequest req) {
		String username = null;
    	String authToken = null;
    	try {
	        String authHeader = req.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            authToken = authHeader.substring(7);
	        }
	        if (authToken == null) {
	            Cookie[] cookies = req.getCookies();
	            if (cookies != null) {
	                authToken = Arrays.stream(cookies).filter(c -> c.getName().equals("platform-auth")).map(Cookie::getValue).findFirst().orElse(null);
	            }
	        }
	        if (authToken != null) {
	        	username = jwtTokenProvider.getUsernameFromToken(authToken);	
	        }
		} catch (IllegalArgumentException e) {
			log.error("Invalid token", e);
		} catch (ExpiredJwtException e) {
			log.warn("The token has expired", e);
		} catch (SignatureException e) {
			log.error("Authentication Failed. Username or Password not valid.");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtTokenProvider.validateToken(authToken)) {
				UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthenticationToken(authToken,
						userService.loadUserByUsername(username));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}		
    }

}
