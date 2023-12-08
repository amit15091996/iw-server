package com.intallysh.widom.controller;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.UpdateUserReqDto;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.exception.ForbiddenException;
import com.intallysh.widom.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PutMapping("/update-profile")
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody @Valid UpdateUserReqDto reqDto) {
		Map<String, Object> map = new HashMap<>();
		try {
			User currentUserDetails = SecurityUtil.getCurrentUserDetails();
			if (currentUserDetails.getUserId() != reqDto.getUserId()) {
				throw new ForbiddenException("You are not allowed to update");
			}
			map = userService.updateProfile(reqDto);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new ForbiddenException("Session Expired ! login and try again");
		}
		return ResponseEntity.ok().body(map);
	}

}
