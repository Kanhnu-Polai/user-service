package com.skillverify.userservice.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillverify.userservice.dto.UserDataDto;
import com.skillverify.userservice.entity.UserData;
import com.skillverify.userservice.exception.UserNotFoundException;
import com.skillverify.userservice.repository.UserDataRepository;

@Service
public class UserDataServiceImpl implements UserDataService {


    private final UserDataRepository repository;
    
    private final ModelMapper mapper;
    
    
    public UserDataServiceImpl(UserDataRepository repository,ModelMapper mapper) {
    	this.repository = repository;
    	this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDataDto getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        UserData userData = repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        return convertToDto(userData);
    }

    @Override
    @Transactional
    public UserDataDto updateUserData(String email, UserDataDto updateData) {
    	
        UserData existingUser = repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

   
        
        mapper.map(updateData,existingUser);
        

        UserData updatedUser = repository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserData(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        UserData userData = repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        repository.delete(userData);
    }

    @Override
    @Transactional
    public UserDataDto addUserData(UserDataDto data) {
        if (data == null || data.getEmail() == null || data.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User data or email cannot be null or empty");
        }

        Optional<UserData> existingUser = repository.findByEmail(data.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + data.getEmail() + " already exists");
        }

        UserData userData = convertToEntity(data);
        UserData savedUser = repository.save(userData);
        return convertToDto(savedUser);
    }

    private UserData convertToEntity(UserDataDto data) {
        return mapper.map(data, UserData.class);
    }

    private UserDataDto convertToDto(UserData userData) {
        return mapper.map(userData, UserDataDto.class);
    }



}


