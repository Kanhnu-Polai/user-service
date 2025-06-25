package com.skillverify.userservice.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable

public class Address {
	
	    private String street;
	    private String city;
	    private String state;
	    private String postalCode;
	    private String country;

}
