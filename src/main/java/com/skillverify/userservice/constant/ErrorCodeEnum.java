package com.skillverify.userservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
	INVALID_TOKEN_ERROR("2000","Invalid token Exception"),
	TOKEN_VALIDATION_ERROR("2001","Token Validation Error"),
	UNAUTHORIZED_ERROR("2002","Unauthorised user");
	
	
	
	private final String code;
	private final String message;
	

}
