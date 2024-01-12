package com.intallysh.widom.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intallysh.widom.dto.BlogReqDto;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.service.blog.BlogService;


@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@GetMapping("/get-blog/{blogId}")
	public ResponseEntity<Map<String, Object>> getBlog(@PathVariable String blogId) {
		return ResponseEntity.ok().body(this.blogService.getBlog(blogId));
	}

	@PostMapping("/create-blog")
	public ResponseEntity<Map<String, Object>> createBlog(@ModelAttribute BlogReqDto blogReqDto) {
		return ResponseEntity.ok().body(this.blogService.createBlog(blogReqDto));
	}
	
	@PutMapping("/update-blog")
	public ResponseEntity<Map<String, Object>> updateBlog(@RequestBody BlogReqDto blogReqDto) {
		String blogId = blogReqDto.getBlogId();
		if(blogId == null) {
			throw new ResourceNotProcessedException("Blog Id is required ...");
		}
		return ResponseEntity.ok().body(this.blogService.createBlog(blogReqDto));
	}
	
	@DeleteMapping("/delete-blog/{blogId}")
	public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable String blogId) {
		
		return ResponseEntity.ok().body(this.blogService.deleteBlog(blogId));
	}
	
	@GetMapping("/blogs")
	public ResponseEntity<Map<String, Object>> getBlogs(){
		return null;
	}
}
