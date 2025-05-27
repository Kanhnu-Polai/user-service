package com.skillverify.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillverify.userservice.dto.UserDataDto;
import com.skillverify.userservice.exception.UnauthorizedException;
import com.skillverify.userservice.service.UserDataService;
import com.skillverify.userservice.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserDataController {

	private final UserDataService userDataService;
	private final JwtUtil jwtUtil;

	public UserDataController(UserDataService userDataService, JwtUtil jwtUtil) {
		this.userDataService = userDataService;
		this.jwtUtil = jwtUtil;
	}
	
	private final String className = this.getClass().getSimpleName();

	@PostMapping("/create")
	public ResponseEntity<UserDataDto> createUser(
			@Valid @RequestBody UserDataDto userDataDto,
			@RequestHeader("Authorization") String authHeader) {
		
		log.info("{} || {} : Received request to create user with email: {}","UserDataController","createUser",userDataDto.getEmail());
		
		String token = extractToken(authHeader);

		log.info("{} || {} : Extracted token: {}","UserDataController","createUser",token);
		String tokenEmail = jwtUtil.getEmailFromToken(token);
		String tokenRole = jwtUtil.getRoleFromToken(token);
	    log.info("{} || {} : Token email: {}, Token role: {}", "UserDataController", "createUser", tokenEmail, tokenRole);

		if (!"ADMIN".equals(tokenRole) && !tokenEmail.equals(userDataDto.getEmail())) {
			log.warn("{} || {} : Unauthorized access attempt by {} with role {}", "UserDataController","createUser", tokenEmail, tokenRole);
		
			throw new UnauthorizedException("You are not authorized to create or update this user's data.");
		}

	
		UserDataDto createdUser = userDataService.addUserData(userDataDto);
		log.info("{} || {} : User created successfully: {}", "UserDataController", "createUser", createdUser.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
	
	
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDataDto> getUserByEmail(
			@PathVariable String email,
			@RequestHeader("Authorization") String authHeader){
		
		String methodName = "getUserByEmail";
		
		log.info("{} || {} : Received request to create user with email : {}",className,methodName,email);
		
		
		
	
		
		String token = extractToken(authHeader);
		log.info("{} || {} : Extracted token: {}",className,methodName,token);
		
		String tokenEmail = jwtUtil.getEmailFromToken(token);
		
		log.info("{} || {} : Token email: {} ",className,methodName, tokenEmail);
		
		if(!tokenEmail.equals(email)) {
			log.warn("{} || {} : Unauthorized access attempt by {} ", className,methodName, email);
			throw new UnauthorizedException("The provided email and extracted email id not same");
		}
		
		
		UserDataDto userData = userDataService.getUserByEmail(email);
		
		
		log.info("{} || {} : Revevied user data: {} ",className,methodName,userData);
		
		return ResponseEntity.status(HttpStatus.OK).body(userData);
		
	}
	
	
	
	@PostMapping("/update/{email}")
	public ResponseEntity<UserDataDto> updateUser(
			@PathVariable String email,
			@RequestBody UserDataDto updateData,
			@RequestHeader("Authorization") String authHeader){
		
		String methodName = "UpdateUser";
	
		
		log.info("{} || {}() : Received request to update user with user data : {}",className,methodName,updateData);
		
		String token = extractToken(authHeader);
		
		String tokenEmail = jwtUtil.getEmailFromToken(token);
		
		log.info("{} || {}() : Token extracted from jwtUtils class : {}",className,methodName,tokenEmail);
		
		
		if(!tokenEmail.equals(email)) {
			log.warn("{} || {}() :  Unauthorized access attempt by {}",className,methodName,email);
		}
		
		UserDataDto updateUser = userDataService.updateUserData(email, updateData);
		
		return ResponseEntity.status(HttpStatus.OK).body(updateUser);
	}
	
	
	

	private String extractToken(String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new UnauthorizedException("Invalid or missing Authorization header");
		}
		log.info(authHeader.substring(7));
		return authHeader.substring(7);
	}
}