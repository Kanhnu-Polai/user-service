package com.skillverify.userservice.controller;

import com.skillverify.userservice.dto.UserDataDto;
import com.skillverify.userservice.service.UserDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") 
@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService userDataService;

    

    private final String className = this.getClass().getSimpleName();

    /**
     * Create new user
     */
    @PostMapping("/create")
    public ResponseEntity<UserDataDto> createUser(
            @Valid @RequestBody UserDataDto userDataDto) {
        
        String method = "createUser";
        log.info("{} || {} : Request to create user: {}", className, method, userDataDto.getEmail());

        UserDataDto createdUser = userDataService.addUserData(userDataDto);

        log.info("{} || {} : User created successfully: {}", className, method, createdUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Get user by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDataDto> getUserByEmail(@PathVariable String email) {
        String method = "getUserByEmail";
        log.info("{} || {} : Fetching user by email: {}", className, method, email);

        UserDataDto userData = userDataService.getUserByEmail(email);

        log.info("{} || {} : User fetched: {}", className, method, userData.getEmail());
        return ResponseEntity.ok(userData);
    }

    /**
     * Update user by email
     */
    @PutMapping("/update/{email}")
    public ResponseEntity<UserDataDto> updateUser(
            @PathVariable String email,
            @Valid @RequestBody UserDataDto updateData) {
        
        String method = "updateUser";
        log.info("{} || {} : Updating user with email: {}", className, method, email);

        UserDataDto updatedUser = userDataService.updateUserData(email, updateData);

        log.info("{} || {} : User updated: {}", className, method, updatedUser.getEmail());
        return ResponseEntity.ok(updatedUser);
    }
}