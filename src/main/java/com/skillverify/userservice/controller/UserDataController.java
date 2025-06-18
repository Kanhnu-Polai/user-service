package com.skillverify.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        String methodName = "createUser";
        log.info("{} || {} : Received request to create user with email: {}", className, methodName, userDataDto.getEmail());

        String token = extractToken(authHeader);
        String tokenEmail = jwtUtil.getEmailFromToken(token);
        String tokenRole = jwtUtil.getRoleFromToken(token);

        log.info("{} || {} : Token email: {}, Token role: {}", className, methodName, tokenEmail, tokenRole);

        if (!"ADMIN".equals(tokenRole) && !tokenEmail.equals(userDataDto.getEmail())) {
            log.warn("{} || {} : Unauthorized access attempt by {} with role {}", className, methodName, tokenEmail, tokenRole);
            throw new UnauthorizedException("You are not authorized to create or update this user's data.");
        }

        UserDataDto createdUser = userDataService.addUserData(userDataDto);
        log.info("{} || {} : User created successfully: {}", className, methodName, createdUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDataDto> getUserByEmail(
            @PathVariable String email,
            @RequestHeader("Authorization") String authHeader) {

        String methodName = "getUserByEmail";
        log.info("{} || {} : Received request to get user by email: {}", className, methodName, email);

        String token = extractToken(authHeader);
        String tokenEmail = jwtUtil.getEmailFromToken(token);

        log.info("{} || {} : Token email: {}", className, methodName, tokenEmail);

        if (!tokenEmail.equals(email)) {
            log.warn("{} || {} : Unauthorized access attempt by {}", className, methodName, tokenEmail);
            throw new UnauthorizedException("You are not authorized to access this user's data.");
        }

        UserDataDto userData = userDataService.getUserByEmail(email);
        log.info("{} || {} : Retrieved user data: {}", className, methodName, userData);

        return ResponseEntity.status(HttpStatus.OK).body(userData);
    }

    @PostMapping("/update/{email}")
    public ResponseEntity<UserDataDto> updateUser(
            @PathVariable String email,
            @RequestBody UserDataDto updateData,
            @RequestHeader("Authorization") String authHeader) {

        String methodName = "updateUser";
        log.info("{} || {} : Received request to update user with email: {}", className, methodName, email);

        String token = extractToken(authHeader);
        String tokenEmail = jwtUtil.getEmailFromToken(token);

        log.info("{} || {} : Token email: {}", className, methodName, tokenEmail);

        if (!tokenEmail.equals(email)) {
            log.warn("{} || {} : Unauthorized access attempt by {}", className, methodName, tokenEmail);
            throw new UnauthorizedException("You are not authorized to update this user's data.");
        }

        UserDataDto updatedUser = userDataService.updateUserData(email, updateData);
        log.info("{} || {} : User updated successfully: {}", className, methodName, updatedUser);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("{} || extractToken : Invalid or missing Authorization header", className);
            throw new UnauthorizedException("Invalid or missing Authorization header");
        }

        String token = authHeader.substring(7);
        log.info("{} || extractToken : Token extracted: {}", className, token);
        return token;
    }
}