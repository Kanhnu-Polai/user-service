package com.skillverify.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.skillverify.userservice.constant.ErrorCodeEnum;
import com.skillverify.userservice.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TokenValidationException.class)
	public ResponseEntity<ErrorResponse> handleTokenValidationException(TokenValidationException ex){
		ErrorResponse errorResponse = new ErrorResponse(
				ErrorCodeEnum.TOKEN_VALIDATION_ERROR.getCode(),
				ErrorCodeEnum.TOKEN_VALIDATION_ERROR.getMessage());
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
	
	
	@ExceptionHandler(InvalidTokenException.class)
	public  ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex){
		ErrorResponse response = new ErrorResponse(
				ErrorCodeEnum.INVALID_TOKEN_ERROR.getCode(),
				ErrorCodeEnum.INVALID_TOKEN_ERROR.getMessage());
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	
	
	


}
