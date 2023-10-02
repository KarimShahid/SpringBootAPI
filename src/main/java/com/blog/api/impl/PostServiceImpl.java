package com.blog.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.api.entities.Category;
import com.blog.api.entities.Post;
import com.blog.api.entities.User;
import com.blog.api.exceptions.ResouceNotFoundException;
import com.blog.api.payloads.PostDto;
import com.blog.api.payloads.PostResponse;
import com.blog.api.repositories.CategoryRepo;
import com.blog.api.repositories.PostRepo;
import com.blog.api.repositories.UserRepo;
import com.blog.api.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	// Create Method
	@Override
	public PostDto createPost(PostDto postDto, int userId, int categoryId) {
		// Finding user id from user entity
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResouceNotFoundException("User", "User Id", userId));

		// Finding category id from category entity
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "Category Id", categoryId));

		// mapping the postDto to post
		Post post = mapper.map(postDto, Post.class);

		// setting the attributes
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		// Saving the post
		Post save = this.postRepo.save(post);

		// Mapping post into postDTO and returning it
		return mapper.map(save, PostDto.class);
	}

	// Update Method
	@Override
	public PostDto updatePost(PostDto postDto, int id) {
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResouceNotFoundException("Post", "Post Id", id));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post udpatedPost = postRepo.save(post);

		return mapper.map(udpatedPost, PostDto.class);
	}

	// Delete Method
	@Override
	public void deletePost(int id) {
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResouceNotFoundException("Post", "Post Id", id));
		this.postRepo.delete(post);
	}

	// Getting All posts
	@Override
	public PostResponse getAllPosts(int pageNum, int pageSize, String sortBy, String sortDir) {

		//For sorting direction (ASC or DESC)
		Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
		
		
		// For pagination
		Pageable p = PageRequest.of(pageNum, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> allPosts = pagePost.getContent();

		List<PostDto> listOfPostDto = allPosts.stream().map((post) -> mapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(listOfPostDto);
		postResponse.setPageNum(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	// Get single post by id
	@Override
	public PostDto getPostById(int id) {
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResouceNotFoundException("Post", "Post Id", id));

		return this.mapper.map(post, PostDto.class);
	}

	// Get posts by categoryID
	@Override
	public PostResponse getAllPostsByCategory(int categoryId, int pageNum, int pageSize) {

		// for Pagination
		Pageable p = PageRequest.of(pageNum, pageSize);

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResouceNotFoundException("Category", "Category Id", categoryId));

		Page<Post> pagePost = this.postRepo.findByCategory(category, p);
		List<Post> listOfPosts = pagePost.getContent();

		List<PostDto> listOfPostByCategory = listOfPosts.stream().map((post) -> mapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(listOfPostByCategory);
		postResponse.setPageNum(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;

	}

	// Get posts by userID
	@Override
	public PostResponse getAllPostsByUser(int userId, int pageNum, int pageSize) {
		// for Pagination
		Pageable p = PageRequest.of(pageNum, pageSize);

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResouceNotFoundException("User", "User Id", userId));

		Page<Post> pagePost = this.postRepo.findByUser(user, p);

		List<Post> findByUser = pagePost.getContent();

		List<PostDto> listOfPostByUser = findByUser.stream().map((post) -> mapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(listOfPostByUser);
		postResponse.setPageNum(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> listOfPosts = this.postRepo.findByTitleContaining(keyword);
		
		List<PostDto> postDto = listOfPosts.stream().map((post) -> mapper.map(post, PostDto.class))
		.collect(Collectors.toList());
		
		return postDto;
	}

}
