package com.intallysh.widom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.intallysh.widom.entity.UserActivity;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.UserActivityRepo;
import com.intallysh.widom.service.UserActivityService;

@Service
public class UserActivityServiceImpl implements UserActivityService{

	@Autowired
	private UserActivityRepo activityRepo;
	
	
	@Override
	public Map<String, Object> getUserActivity(long userId, Pageable paging) {
		Map<String, Object> map = new HashMap<>();
		try {
			Page<UserActivity> page = activityRepo.findAllByUserId(userId,paging);
			if (page.hasContent()) {
				map.put("message", "Data Fetched Successfully ...");
				map.put("activityDetails", page.getContent());
				
			} else {
				map.put("message", "Data not available ...");
				map.put("activityDetails", new ArrayList<>());
			}
			map.put("totalPages", page.getTotalPages());
			map.put("totalResults", page.getTotalElements());
			map.put("currentPage", page.getNumber());
			map.put("noOfElements", page.getNumberOfElements());
			map.put("isLastPage", page.isLast());
			map.put("isFirstPage", page.isFirst());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}
		map.put("status", "Success");
		return map;
	}

}
