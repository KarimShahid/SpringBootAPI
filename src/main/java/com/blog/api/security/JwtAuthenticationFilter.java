package com.blog.api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	// This method is called every time an api is called
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. Get token from request

		// The token will look like this= Authorization = Bearer whwhohowhwowh;
		String requestToken = request.getHeader("Authorization");
		System.out.println(requestToken);

		String username = null;

		String token = null;

		if (requestToken != null && requestToken.startsWith("Bearer")) {
			// This is token without Bearer attached to it.
			token = requestToken.substring(7);

			try {
				username = this.jwtTokenHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("unable to get JWT token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT has expired!");
			} catch (MalformedJwtException e) {
				System.out.println("Invalid JWT");
			}
		} else {
			System.out.println("Jwt is null or does not begin with Bearer");
		}
		
		
		

		// 2. Validate Token once token is received.

		// Username should not be null AND SecurityContextHolder should not have user in
		// it
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// validating
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (this.jwtTokenHelper.validateToken(token, userDetails)) {

				// If everything is ok
				// Authentication garnu parcha aba

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource()
								.buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				// If not validated
				System.out.println("Invalid JWT token");
			}

		} else {
			System.out.println("Username is not null OR context is not null");
		}

		
		filterChain.doFilter(request, response);
	}

}
