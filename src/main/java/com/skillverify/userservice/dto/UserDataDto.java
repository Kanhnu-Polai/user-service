package com.skillverify.userservice.dto;

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
	    private String resumeLink;
	    private String photoUrl;
	    private String role;

}
