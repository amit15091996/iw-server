package com.intallysh.widom.service.blog.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intallysh.widom.entity.blog.Blog;
import com.intallysh.widom.repo.BlogRepo;

@Service
public class BlogServiceImpl {

	@Autowired
	private BlogRepo blogRepo;
	
	public Map<String, Object> getBlog(String id) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.blogRepo.findById(id).get();
		map.put("blog", blog);
		return map;
	}
	
	
}
