package com.intallysh.widom.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

public interface UserActivityService {

	public Map<String, Object> getUserActivity(long userId, Pageable paging);
	
	
}
