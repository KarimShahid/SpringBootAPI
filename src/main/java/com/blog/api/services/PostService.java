package com.blog.api.services;

import java.util.List;

import com.blog.api.payloads.PostDto;
import com.blog.api.payloads.PostResponse;

public interface PostService  {

	//Create
	PostDto createPost(PostDto postDto, int categoryId, int userId);
	
	
	//Update
	PostDto updatePost(PostDto postDto, int id);
	
	
	//Delete
	void deletePost(int id);
	
	
	//Get All
	PostResponse getAllPosts(int pageNum, int pageSize, String sortBy, String sortDir);
	
	
	//Get single
	PostDto getPostById(int id);
	
	
	//Get all post by category
	PostResponse getAllPostsByCategory(int categoryId, int pageNum, int pageSize) ;
	
	
	//Get all post by user
	PostResponse getAllPostsByUser(int userId, int pageNum, int pageSize);
	
	
	//Searching Posts
	List<PostDto> searchPosts(String keyword);
	
}
