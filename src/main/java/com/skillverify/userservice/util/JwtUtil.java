package com.skillverify.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.skillverify.userservice.dto.AuthResponse;
import com.skillverify.userservice.exception.InvalidTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    private final RestClient restClient;
   
    
   @Value("${auth.service.base-url}")
    private String authServiceBaseUrl;

    public AuthResponse getAuthDetails(String token) {
    	
    	 HttpHeaders headers = new HttpHeaders();
    	 headers.set("Authorization", "Bearer " + token);
    	 
    	 log.info("Generated token {}",token);
        try {
            return restClient.get()
                    .uri(authServiceBaseUrl+"/api/auth/validate")
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