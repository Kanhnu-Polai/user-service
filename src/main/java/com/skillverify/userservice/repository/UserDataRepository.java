package com.skillverify.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillverify.userservice.entity.UserData;
import java.util.List;


@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
	
	Optional<UserData>  findByEmail(String email);

}
