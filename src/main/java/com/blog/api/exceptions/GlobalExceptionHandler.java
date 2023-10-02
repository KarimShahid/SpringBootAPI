package com.blog.api.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.api.payloads.ApiResponse;


//This annotation will make this class an exception handler.
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResouceNotFoundException.class)  //() --> to specify which type of exception to handle
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResouceNotFoundException ex){
		
		String message = ex.getMessage();
		
		ApiResponse apiResponse = new ApiResponse(message, false, new Date());
		
		//return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		Map<String, String> res = new HashMap<>();
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		
		//Iterating thorugh the list of errors
		for (ObjectError error : allErrors) {
			String fieldName = ((FieldError)error).getField();
			
			String message = error.getDefaultMessage();
			
			
			res.put(fieldName, message);
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		
	}
	
	
	@ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }
}
