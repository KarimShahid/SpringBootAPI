package com.blog.api.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	
	private int categoryId;
	
	@NotEmpty
	@NotBlank
	@Size(min = 4, message = "Title must be a minimum of 4 characters")
	private String categoryTitle;
	
	@NotEmpty
	@NotBlank
	private String categoryDescription;
}
