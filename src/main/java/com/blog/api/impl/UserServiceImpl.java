package com.blog.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.entities.User;
import com.blog.api.exceptions.ResouceNotFoundException;
import com.blog.api.payloads.UserDto;
import com.blog.api.repositories.UserRepo;
import com.blog.api.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper mapper;

	// Create new user
	@Override
	public UserDto createUser(UserDto userDto) {
		// Converting dto to user
		User user = this.dtoToUser(userDto);

		User save = userRepo.save(user);

		// Converting user back to dto
		return this.userToDto(save);
	}

	// Update user
	@Override
	public UserDto updateUser(UserDto userDto, int id) {
		User user = this.userRepo.findById(id).orElseThrow(() -> new ResouceNotFoundException("User", "id", id));

		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());

		User updatedUser = this.userRepo.save(user);

		return this.userToDto(updatedUser);
	}

	// Getting user by id
	@Override
	public UserDto getUserById(int id) {
		User user = this.userRepo.findById(id).orElseThrow(() -> new ResouceNotFoundException("User", "id", id));
		return this.userToDto(user);
	}

	// Getting all users
	@Override
	public List<UserDto> getAllUser() {
		List<User> allUsers = this.userRepo.findAll();
		List<UserDto> listOfUserDtos = allUsers.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return listOfUserDtos;
	}

	// Deleting user
	@Override
	public void deleteUser(int id) {
		User user = this.userRepo.findById(id).orElseThrow(() -> new ResouceNotFoundException("User", "id", id));
		this.userRepo.delete(user);

	}

	/*
	 * This for DTO! Model Mapping. Taking data of one model to another model
	 */
	public User dtoToUser(UserDto userDto) {
//		User user = new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());

		/* Using Model Mapper */
		User user = this.mapper.map(userDto, User.class);
		return user;
	}

	public UserDto userToDto(User user) {
//		UserDto userDto = new UserDto();
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());

		/* Using Model Mapper */
		UserDto userDto = mapper.map(user, UserDto.class);
		return userDto;

	}

}
