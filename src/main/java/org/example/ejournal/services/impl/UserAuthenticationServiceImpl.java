package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.response.LoginResponseDto;
import org.example.ejournal.dtos.response.UserDtoResponse;
import org.example.ejournal.entities.User;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.repositories.UserRepository;
import org.example.ejournal.security.TokenService;
import org.example.ejournal.services.UserAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
	
	private final UserAuthenticationRepository userAuthenticationRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	
	public UserAuthenticationServiceImpl(UserAuthenticationRepository userAuthenticationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
		this.userAuthenticationRepository = userAuthenticationRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
	}
	
	@Override
	public UserDtoResponse changeUserRole(long userId, RoleType role) {
		if (userAuthenticationRepository.findById(userId).isPresent()) {
			UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId).get();
			
			// set role
			userAuthentication.setRole(role);
			
			userAuthenticationRepository.save(userAuthentication);
		}
		return null;
	}
	
	@Transactional
	@Override
	public UserAuthentication register(AdminRegisterDtoRequest userDto) {
		// Check if the username is already taken
		if (userAuthenticationRepository.findByUsername(userDto.getUsername()).isEmpty()) {
			// Create new UserAuthentication object
			UserAuthentication userAuthentication = new UserAuthentication();
			userAuthentication.setUsername(userDto.getUsername());
			userAuthentication.setPassword(passwordEncoder.encode(userDto.getPassword())); // encode password
			userAuthentication.setRole(userDto.getRole());
			
			// Persist UserAuthentication entity
			userAuthenticationRepository.save(userAuthentication);
			
			// Return the created UserAuthentication object
			return userAuthentication;
		} else {
			throw new IllegalStateException("Username already exists");
		}
	}
	
	@Override
	public LoginResponseDto login(String username, String password) {
		try {
			// Authenticate the user
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password)
			);
			
			// Generate JWT token
			String token = tokenService.generateJwt(auth);
			
			// Find user's role from the db
			UserAuthentication user = userAuthenticationRepository.findByUsername(username).get();
			String role = user.getRole().toString();
			long id = user.getId();
			
			// Return the response with JWT token
			return new LoginResponseDto(id, username, role, token);
			
		} catch (AuthenticationException e) {
			throw new IllegalArgumentException("Invalid username or password");
		}
	}
}
