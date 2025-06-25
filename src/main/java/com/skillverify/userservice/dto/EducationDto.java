package com.skillverify.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDto {
	

	    private String level; // Example: 10th, 12th, B.Sc, M.Sc
	    private String institution;
	    private String boardOrUniversity;
	    private String passingYear;
}
