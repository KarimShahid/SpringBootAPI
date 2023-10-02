package com.blog.api.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.entities.Comment;
import com.blog.api.entities.Post;
import com.blog.api.exceptions.ResouceNotFoundException;
import com.blog.api.payloads.CommentDto;
import com.blog.api.repositories.CommentRepo;
import com.blog.api.repositories.PostRepo;
import com.blog.api.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, int postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResouceNotFoundException("Post", "Post Id", postId));
		
		Comment comment = mapper.map(commentDto, Comment.class);
		
		comment.setPost(post);
		
		Comment save = this.commentRepo.save(comment);
		
		return mapper.map(save, CommentDto.class);
	}

	@Override
	public void deleteComment(int commentId) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(() -> new ResouceNotFoundException("Comment", "Comment Id", commentId));
		this.commentRepo.delete(comment);
		
	}

}
