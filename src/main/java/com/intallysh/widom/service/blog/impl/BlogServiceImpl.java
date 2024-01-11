package com.intallysh.widom.service.blog.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.BlogReqDto;
import com.intallysh.widom.entity.blog.Blog;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.BlogRepo;
import com.intallysh.widom.service.blog.BlogService;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepo blogRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Map<String, Object> getBlog(String id) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.blogRepo.findById(id)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid Blog Id"));
		map.put("result", blog);
		map.put("status", "Success");
		map.put("message", "Blog data fetched Successfully ...");
		return map;
	}

	@Override
	public Map<String, Object> createBlog(BlogReqDto blogReqDto) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.mapper.map(blogReqDto, Blog.class);
		try {
			blog.setBlogId(UUID.randomUUID().toString());
			blog.setBloggerId(String.valueOf(SecurityUtil.getCurrentUserDetails().getUserId()));
			blog.setModifiedBy(String.valueOf(SecurityUtil.getCurrentUserDetails().getUserId()));
			blog.setBlogUploadedOn(new Timestamp(System.currentTimeMillis()));
			blog.setModifiedOn(new Timestamp(System.currentTimeMillis()));
			
			blog = this.blogRepo.save(blog);
			map.put("message", "Blog created successfully");
			map.put("status", "Success");
			map.put("result", blog);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Blog not created ...");
		}

		return map;
	}
	
//	@Override
	public Map<String, Object> updateBlog(BlogReqDto blogReqDto) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.blogRepo.findById(blogReqDto.getBlogId()).orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid Blog Id"));
		try {
			blog.setBlogDesc1(blogReqDto.getBlogDesc1());
			blog.setBlogTitle(blogReqDto.getBlogTitle());;
			blog.setModifiedOn(new Timestamp(System.currentTimeMillis()));
			
			blog = this.blogRepo.save(blog);
			map.put("message", "Blog created successfully");
			map.put("status", "Success");
			map.put("result", blog);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Blog not created ...");
		}

		return map;
	}

}
