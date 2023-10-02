package com.blog.api.services;

import com.blog.api.payloads.CommentDto;

public interface CommentService {

	//Create
	CommentDto createComment(CommentDto commentDto, int postId);
	
	//Delete
	void deleteComment(int commentId);
	

}
