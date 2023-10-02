package com.blog.api.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.impl.CommentServiceImpl;
import com.blog.api.payloads.ApiResponse;
import com.blog.api.payloads.CommentDto;

@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	private CommentServiceImpl commentServiceImpl;
	
	
	@PostMapping("/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable int postId){
		
		CommentDto createdComment = this.commentServiceImpl.createComment(commentDto, postId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
	}
	
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable int commentId){
		this.commentServiceImpl.deleteComment(commentId);
		return ResponseEntity.ok(new ApiResponse("Comment Deleted Successfully",true, new Date()));
	}
}
