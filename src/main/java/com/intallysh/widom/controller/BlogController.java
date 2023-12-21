package com.intallysh.widom.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intallysh.widom.service.blog.impl.BlogServiceImpl;

@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {
	
	@Autowired
	private BlogServiceImpl blogServiceImpl;

	@GetMapping("/get-blog/{blogId}")
	public ResponseEntity<Map<String, Object>> getBlog(@PathVariable String blogId){
		return ResponseEntity.ok().body(this.blogServiceImpl.getBlog(blogId));
	}
}
