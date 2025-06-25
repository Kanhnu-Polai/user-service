package com.skillverify.userservice.service;

import com.skillverify.userservice.dto.*;
import com.skillverify.userservice.entity.*;
import com.skillverify.userservice.exception.UserNotFoundException;
import com.skillverify.userservice.repository.UserDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository repository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public UserDataDto addUserData(UserDataDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User data or email cannot be null or empty");
        }

        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        }

        UserData user = convertToEntity(dto);
        UserData savedUser = repository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    @Transactional
    public UserDataDto updateUserData(String email, UserDataDto updateDto) {
        UserData existingUser = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        // Update simple fields
        existingUser.setFullName(updateDto.getFullName());
        existingUser.setPhone(updateDto.getPhone());
        existingUser.setBio(updateDto.getBio());
        existingUser.setPhotoUrl(updateDto.getPhotoUrl());
        existingUser.setRole(updateDto.getRole());
        existingUser.setResumeLink(updateDto.getResumeLink());

        // Update Address
        if (updateDto.getAddress() != null) {
            existingUser.setAddress(mapper.map(updateDto.getAddress(), Address.class));
        }

        // Update Education
        if (updateDto.getEducations() != null) {
            List<Education> educations = updateDto.getEducations().stream()
                    .map(e -> {
                        Education edu = mapper.map(e, Education.class);
                        edu.setUser(existingUser);
                        return edu;
                    }).collect(Collectors.toList());

            existingUser.getEducations().clear();
            existingUser.getEducations().addAll(educations);
        }

        // Update Resumes
        if (updateDto.getResumes() != null) {
            List<Resume> resumes = updateDto.getResumes().stream()
                    .map(r -> {
                        Resume res = mapper.map(r, Resume.class);
                        res.setUser(existingUser);
                        return res;
                    }).collect(Collectors.toList());

            existingUser.getResumes().clear();
            existingUser.getResumes().addAll(resumes);
        }

        UserData saved = repository.save(existingUser);
        return convertToDto(saved);
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
    public UserDataDto getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        UserData user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        return convertToDto(user);
    }

    // ---------------------- Mapping Helpers ----------------------

    private UserDataDto convertToDto(UserData user) {
        return mapper.map(user, UserDataDto.class);
    }

    private UserData convertToEntity(UserDataDto dto) {
        UserData user = mapper.map(dto, UserData.class);

        if (dto.getEducations() != null) {
            List<Education> educations = dto.getEducations().stream()
                    .map(e -> {
                        Education edu = mapper.map(e, Education.class);
                        edu.setUser(user);
                        return edu;
                    }).collect(Collectors.toList());
            user.setEducations(educations);
        }

        if (dto.getResumes() != null) {
            List<Resume> resumes = dto.getResumes().stream()
                    .map(r -> {
                        Resume res = mapper.map(r, Resume.class);
                        res.setUser(user);
                        return res;
                    }).collect(Collectors.toList());
            user.setResumes(resumes);
        }

        return user;
    }
}