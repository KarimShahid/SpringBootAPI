package com.blog.api.services;

import java.util.List;

import com.blog.api.payloads.CategoryDto;

public interface CategoryService {
	
	//Create
	CategoryDto createCategory(CategoryDto categoryDto);
	
	//Update
	CategoryDto updateCategory(CategoryDto categoryDto, int id);
	
	//Delete
	void deleteCategory(int id);
	
	//Get by id
	CategoryDto getCategoryById(int id);
	
	//Get all
	List<CategoryDto> getAllCategory();
}
