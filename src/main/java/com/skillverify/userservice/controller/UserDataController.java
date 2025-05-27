package com.skillverify.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<UserDataDto> createUser(@Valid @RequestBody UserDataDto userDataDto,
                                                  @RequestHeader("Authorization") String authHeader) {
        // 1. Extract JWT token from Authorization header
    	log.info("Received request to create user with email: {}", userDataDto.getEmail());
    	
    	log.info(authHeader);
        String token = extractToken(authHeader);
        ;
   
        log.info("Generated token {} ", token);

        // 2. Get email and role from JWT token (calls auth-service internally)
        String tokenEmail = jwtUtil.getEmailFromToken(token);
        String tokenRole = jwtUtil.getRoleFromToken(token);

 log.info("Token email: {}, Token role: {}", tokenEmail, tokenRole);
        // 3. Allow if token email matches request email OR if user is ADMIN
        if (!"ADMIN".equals(tokenRole) && !tokenEmail.equals(userDataDto.getEmail())) {
            // 5. Else throw 403 Forbidden (UnauthorizedException)
            throw new UnauthorizedException("You are not authorized to create or update this user's data.");
        }

        // 4. If allowed, create/update user data
        UserDataDto createdUser = userDataService.addUserData(userDataDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid or missing Authorization header");
        }
        log.info(authHeader.substring(7));
        return authHeader.substring(7);
    }
}