package com.blog.api.services;

import java.util.List;

import com.blog.api.payloads.UserDto;

public interface UserService {
	
	//Creating user
	UserDto createUser(UserDto userDto);
	
	//Updating
	UserDto updateUser(UserDto userDto, int id);
	
	//Getting user by id
	UserDto getUserById(int id);
	
	//Get all user
	List<UserDto> getAllUser();
	
	//Deleting by id
	void deleteUser(int id);
}
