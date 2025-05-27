package com.skillverify.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.skillverify.userservice.dto.AuthResponse;
import com.skillverify.userservice.exception.InvalidTokenException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;

@Component
@Slf4j
public class JwtUtil {

    private final RestClient restClient;
    private final String authServiceUrl;


    public JwtUtil(@Value("${auth.service.validate.url}") String authServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(authServiceUrl)
                .build();
        this.authServiceUrl = authServiceUrl;
    }

    
   

    public AuthResponse getAuthDetails(String token) {
    	
    	 HttpHeaders headers = new HttpHeaders();
    	 headers.set("Authorization", "Bearer " + token);
    	 
    	 log.info("Generated token {}",token);
        try {
            return restClient.get()
                    .uri("http://localhost:8080/api/auth/validate")
                    .headers(h->h.addAll(headers))
                    .retrieve()
                    .body(AuthResponse.class);
        } catch (Exception e) {
            throw new InvalidTokenException("Token validation failed: " + e.getMessage());
        }
    }

    public String getRoleFromToken(String token) {
        return getAuthDetails(token).getRole();
    }

    public String getEmailFromToken(String token) {
        return getAuthDetails(token).getEmail();
    }

    
   
}