package com.intallysh.widom.service.blog;

import java.util.Map;

import com.intallysh.widom.dto.BlogReqDto;

public interface BlogService {

	Map<String, Object> getBlog(String id);
	Map<String, Object> createBlog(BlogReqDto blogReqDto);
	Map<String, Object> deleteBlog(String blogId);
	Map<String, Object> updateBlog(BlogReqDto blogReqDto);
	
	
	
}
