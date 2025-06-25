package com.skillverify.userservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataDto {
	private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String bio;
    private String photoUrl;
    private String role;
    private String resumeLink;

    private AddressDto address;

    private List<ResumeDto> resumes;
    private List<EducationDto> educations;
}
