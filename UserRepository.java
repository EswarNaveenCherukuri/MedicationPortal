package com.manage.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.hospital.model.User;

//Repository for users
public interface UserRepository extends JpaRepository<User, Integer>{

	// Find the user with given email and password
	public User findByEmailAndPassword(String email, String password);
	
	// Find the users with given role
	public List<User> findAllByRole(String role);


}
