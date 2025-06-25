package com.skillverify.userservice.controller;

import com.skillverify.userservice.dto.ResumeDto;
import com.skillverify.userservice.dto.UserDataDto;
import com.skillverify.userservice.service.CloudinaryService;
import com.skillverify.userservice.service.UserDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users/upload")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final UserDataService userDataService;
    private final CloudinaryService cloudinaryService;

    @PostMapping("/resume")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) {

        try {
            String url = cloudinaryService.uploadFile(file, "resumes");
            log.info("Resume uploaded to Cloudinary: {}", url);

            UserDataDto existingUser = userDataService.getUserByEmail(email);

            // Create a new Resume DTO
            ResumeDto resumeDto = new ResumeDto();
            resumeDto.setResumeLink(url);
            resumeDto.setResumeTitle("Uploaded Resume"); // or use file.getOriginalFilename()

            List<ResumeDto> updatedResumes = new ArrayList<>();
            if (existingUser.getResumes() != null) {
                updatedResumes.addAll(existingUser.getResumes());
            }
            updatedResumes.add(resumeDto);
            existingUser.setResumes(updatedResumes);

            UserDataDto updatedUser = userDataService.updateUserData(email, existingUser);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            log.error("Resume upload failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Resume upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/photo")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) {

        try {
            String url = cloudinaryService.uploadFile(file, "photos");
            log.info("Photo uploaded to Cloudinary: {}", url);

            UserDataDto existingUser = userDataService.getUserByEmail(email);
            existingUser.setPhotoUrl(url);

            UserDataDto updatedUser = userDataService.updateUserData(email, existingUser);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            log.error("Photo upload failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Photo upload failed: " + e.getMessage());
        }
    }
}