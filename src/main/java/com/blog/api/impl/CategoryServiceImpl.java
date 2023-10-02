package com.blog.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.entities.Category;
import com.blog.api.exceptions.ResouceNotFoundException;
import com.blog.api.payloads.CategoryDto;
import com.blog.api.repositories.CategoryRepo;
import com.blog.api.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper mapper;

	// For Adding Category
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.dtoToCategory(categoryDto);
		Category save = this.categoryRepo.save(category);
		return this.categoryToDto(save);
	}

	// Update Category
	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, int id) {

		Category category = this.categoryRepo.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "Category Id", id));

		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());

		Category updatedCat = this.categoryRepo.save(category);

		return this.categoryToDto(updatedCat);

	}

	// Delete
	@Override
	public void deleteCategory(int id) {
		Category category = this.categoryRepo.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "Category Id", id));
		this.categoryRepo.delete(category);

	}

	// Get by Id
	@Override
	public CategoryDto getCategoryById(int id) {
		Category category = this.categoryRepo.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "Category Id", id));
		return this.categoryToDto(category);

	}

	// Get all
	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> allCategories = this.categoryRepo.findAll();

		List<CategoryDto> listOfCategoriesDto = allCategories.stream().map((cat) -> this.categoryToDto(cat))
				.collect(Collectors.toList());

		return listOfCategoriesDto;
	}

	/* This for DTO! Model Mapping. Taking data of one model to another model */

	// Changing CategoryDTO to category
	public Category dtoToCategory(CategoryDto categoryDto) {
		Category category = mapper.map(categoryDto, Category.class);
		return category;
	}

	// Changing Category to categoryDTO
	public CategoryDto categoryToDto(Category category) {
		CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
		return categoryDto;
	}

}
