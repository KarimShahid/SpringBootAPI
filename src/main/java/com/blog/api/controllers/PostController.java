package com.blog.api.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.api.config.AppConstants;
import com.blog.api.impl.FileServiceImpl;
import com.blog.api.impl.PostServiceImpl;
import com.blog.api.payloads.ApiResponse;
import com.blog.api.payloads.PostDto;
import com.blog.api.payloads.PostResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostServiceImpl postServiceImpl;

	@Autowired
	private FileServiceImpl fileServiceImpl;

	@Value("${project.image}")
	private String path;

	// Creating new posts
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @PathVariable int userId,
			@PathVariable int categoryId) {

		PostDto createdPost = this.postServiceImpl.createPost(postDto, userId, categoryId);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
	}

	// Getting Posts by User
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostByUser(
			@RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@PathVariable int userId) {
		PostResponse allPostsByUser = this.postServiceImpl.getAllPostsByUser(userId, pageNum, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(allPostsByUser);
	}

	// Getting Posts by Category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostByCategory(
			@RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUM, required = false) int pageNum,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
			@PathVariable int categoryId) {
		PostResponse allPostsByCategory = this.postServiceImpl.getAllPostsByCategory(categoryId, pageNum, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(allPostsByCategory);
	}

	// Getting all post
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(

			// This is for pagination
			// Page num starts from 0
			@RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUM, required = false) int pageNum,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse allPosts = this.postServiceImpl.getAllPosts(pageNum, pageSize, sortBy, sortDir);

		return ResponseEntity.status(HttpStatus.OK).body(allPosts);
	}

	// Get posts by postId
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getSinglePostById(@PathVariable int postId) {
		PostDto postById = this.postServiceImpl.getPostById(postId);
		return ResponseEntity.status(HttpStatus.OK).body(postById);
	}

	// Delete Posts by id
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable int postId) {
		this.postServiceImpl.deletePost(postId);
		return ResponseEntity.ok(new ApiResponse("User Deleted Successfully!", true, new Date()));
	}

	// Update the post by id
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable int postId) {

		PostDto updatePost = this.postServiceImpl.updatePost(postDto, postId);
		return ResponseEntity.ok(updatePost);
	}

	// Search by title
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String keyword) {

		List<PostDto> searchPosts = this.postServiceImpl.searchPosts(keyword);
		return ResponseEntity.ok(searchPosts);
	}

	// Post Image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable int postId) throws IOException {

		PostDto postDto = this.postServiceImpl.getPostById(postId);
		
		String fileName = this.fileServiceImpl.uploadImage(path, image);
		postDto.setImageName(fileName);
		
		PostDto updatedPost = this.postServiceImpl.updatePost(postDto, postId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedPost);
	}
	
	
	//serve files
	@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(
			@PathVariable String imageName,
			HttpServletResponse response) throws IOException{
		InputStream resource = this.fileServiceImpl.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		
	}

}
