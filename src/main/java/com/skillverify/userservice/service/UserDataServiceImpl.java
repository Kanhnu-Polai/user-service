package com.skillverify.userservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillverify.userservice.dto.UserDataDto;
import com.skillverify.userservice.entity.UserData;
import com.skillverify.userservice.exception.UserNotFoundException;
import com.skillverify.userservice.repository.UserDataRepository;

@Service
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    private UserDataRepository repository;

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
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (updateData == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        UserData existingUser = repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

   
        if (updateData.getFullName() != null) {
            existingUser.setFullName(updateData.getFullName());
        }
        if (updateData.getPhone() != null) {
            existingUser.setPhone(updateData.getPhone());
        }
        if (updateData.getBio() != null) {
            existingUser.setBio(updateData.getBio());
        }
        if (updateData.getResumeLink() != null) {
            existingUser.setResumeLink(updateData.getResumeLink());
        }
        if (updateData.getPhotoUrl() != null) {
            existingUser.setPhotoUrl(updateData.getPhotoUrl());
        }
        if (updateData.getRole() != null) {
            existingUser.setRole(updateData.getRole());
        }

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
        return UserData.builder()
                .id(data.getId())
                .fullName(data.getFullName())
                .email(data.getEmail())
                .phone(data.getPhone())
                .bio(data.getBio())
                .resumeLink(data.getResumeLink())
                .photoUrl(data.getPhotoUrl())
                .role(data.getRole())
                .build();
    }

    private UserDataDto convertToDto(UserData userData) {
        return UserDataDto.builder()
                .id(userData.getId())
                .fullName(userData.getFullName())
                .email(userData.getEmail())
                .phone(userData.getPhone())
                .bio(userData.getBio())
                .resumeLink(userData.getResumeLink())
                .photoUrl(userData.getPhotoUrl())
                .role(userData.getRole())
                .build();
    }



}


