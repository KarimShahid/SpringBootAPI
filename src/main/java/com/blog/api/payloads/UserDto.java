package com.blog.api.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	//Contains the attributes of User class.
	//WIll only contain those attributes that the user will provide.
	
	private int id;
	
	@NotEmpty
	@Size(min = 4, message = "Username must be a minimum of 4 characters")
	private String name;
	
	@Email(message = "Your email address is not valid!")
	private String email;
	
	@NotEmpty
	@Size(min = 3, max = 10, message = "Password must be a minimum of 3 characters and should not exceed 10 characters")
	private String password;
	
	@NotEmpty
	private String about;
}
