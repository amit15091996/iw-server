package com.intallysh.widom.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PreAuthorize("hasAuthority('ADMIN_ROLE')")
	@PostMapping("/create-blog")
	public ResponseEntity<Map<String, Object>> createBlog(@ModelAttribute BlogReqDto blogReqDto) {
		return ResponseEntity.ok().body(this.blogService.createBlog(blogReqDto));
	}
	
	@PreAuthorize("hasAuthority('ADMIN_ROLE')")
	@PutMapping("/update-blog")
	public ResponseEntity<Map<String, Object>> updateBlog(@ModelAttribute BlogReqDto blogReqDto) {
		String blogId = blogReqDto.getBlogId();
		if(blogId == null) {
			throw new ResourceNotProcessedException("Blog Id is required ...");
		}
		return ResponseEntity.ok().body(this.blogService.updateBlog(blogReqDto));
	}
	
	@PreAuthorize("hasAuthority('ADMIN_ROLE')")
	@DeleteMapping("/delete-blog/{blogId}")
	public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable String blogId) {
		
		return ResponseEntity.ok().body(this.blogService.deleteBlog(blogId));
	}
	
	@GetMapping("/blogs")
	public ResponseEntity<Map<String, Object>> getBlogs(
			@RequestParam(defaultValue = "NOT_DELETED") String type,
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "blogId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortingOrder
            ){
		Sort by = Sort.by(sortBy);
		if(sortingOrder.equals("ASC")) {
			by = Sort.by(sortBy).ascending();
		}else {
			by = Sort.by(sortBy).descending();
		}
		Pageable paging = PageRequest.of(pageNo, pageSize, by);
		return ResponseEntity.ok().body(this.blogService.getBlogs(type, paging));
	}
}
