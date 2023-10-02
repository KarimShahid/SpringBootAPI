package com.blog.api.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.impl.CategoryServiceImpl;
import com.blog.api.payloads.ApiResponse;
import com.blog.api.payloads.CategoryDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	// Creating User
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto createdCategoryDto = this.categoryServiceImpl.createCategory(categoryDto);
		System.out.println(createdCategoryDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoryDto);
	}

	// Updating User
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable int categoryId) {
		CategoryDto updateCategory = this.categoryServiceImpl.updateCategory(categoryDto, categoryId);
		return ResponseEntity.ok(updateCategory);
	}

	// Deleting
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable int categoryId) {
		this.categoryServiceImpl.deleteCategory(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category Deleted Successfully!",true, new Date()), HttpStatus.OK);
		
	}
	
	
	//Get Category by id
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int categoryId){
		CategoryDto categoryById = this.categoryServiceImpl.getCategoryById(categoryId);
		return ResponseEntity.ok(categoryById);
	}
	
	
	//Get all Categories
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		List<CategoryDto> allCategories = this.categoryServiceImpl.getAllCategory();
		return ResponseEntity.ok(allCategories);
	}

}
