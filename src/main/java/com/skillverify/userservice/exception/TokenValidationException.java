package com.skillverify.userservice.exception;

public class TokenValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TokenValidationException(String message) {
		super(message);
	}

}
