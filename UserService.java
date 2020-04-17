package com.manage.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.hospital.model.User;
import com.manage.hospital.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// Method to add user
	public String addUser(User user) {
		String message = "";
		try {
			// Registering new user
			user = userRepository.save(user);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			// On fail checking the cause of the error
			if (e.getMessage().indexOf("constraint") >= 0) {
				message = "Email already exists";
			} else {
				message = "Please try again";
			}
		}
		return message;
	}

	// Method to get authenticate the user with email and password
	public User authenticate(User user) {
		return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
	}
	
	public List<User> findAllByRole(){
		return userRepository.findAllByRole("doctor");
	}
	
	public User getUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}

}
