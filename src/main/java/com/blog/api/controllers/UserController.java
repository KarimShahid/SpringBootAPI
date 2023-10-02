package com.blog.api.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.impl.UserServiceImpl;
import com.blog.api.payloads.ApiResponse;
import com.blog.api.payloads.UserDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserServiceImpl impl;

	
	
	
	// POST - Create user
	@PostMapping("/")
	public ResponseEntity<UserDto> createuser(@Valid @RequestBody UserDto userDto) {

		UserDto createdUserDto = this.impl.createUser(userDto);
		System.out.println(createdUserDto);
		return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
	}
	
	
	

	// PUT - update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") int id) {

		UserDto updateUser = this.impl.updateUser(userDto, id);
		return ResponseEntity.ok(updateUser);
	}

	
	
	//For only admin role
	// Delete - delete user
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") int id) { // The <ApiResponse> is being
																					// returned so we keep it in the <>
		this.impl.deleteUser(id);

		// return ResponseEntity.ok(Map.of("msg","User has been deleted!"));	
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully!",true, new Date()), HttpStatus.OK);

	}
	
	
	
	

	// Get - get all users
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUser() {
		List<UserDto> allUsers = this.impl.getAllUser();
		return new ResponseEntity<List<UserDto>>(allUsers, HttpStatus.OK);
	}
	
	
	
	
	
	// Get - get single user by id
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int userId) {
		UserDto userById = this.impl.getUserById(userId);
		return ResponseEntity.ok(userById);
	}
	
}
