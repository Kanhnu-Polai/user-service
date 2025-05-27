package com.skillverify.userservice.service;

import com.skillverify.userservice.dto.UserDataDto;

public interface UserDataService {
	UserDataDto addUserData(UserDataDto data);
	UserDataDto getUserByEmail(String email);
	UserDataDto updateUserData(String email,UserDataDto updateData);
	void deleteUserData(String email,String password); // also request to auth - service to delete the user

}
