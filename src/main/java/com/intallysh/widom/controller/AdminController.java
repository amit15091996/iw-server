package com.intallysh.widom.controller;

import com.intallysh.widom.dto.FileReqDto;
import com.intallysh.widom.dto.UpdateUserReqDto;
import com.intallysh.widom.exception.ForbiddenException;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.service.FilesDetailService;
import com.intallysh.widom.service.UserService;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Autowired
	private FilesDetailService filesDetailService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/upload-file")
	public ResponseEntity<Map<String, Object>> uploadFile(@Valid @ModelAttribute FileReqDto fileReqDto)
			throws Exception {

		return ResponseEntity.ok().body(filesDetailService.uploadFile(fileReqDto));
	}
	
	
	@PutMapping("/user/update-profile")
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody @Valid UpdateUserReqDto reqDto) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = userService.updateProfile(reqDto);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new ForbiddenException("Session Expired ! login and try again");
		}
		return ResponseEntity.ok().body(map);
	}
	
	@DeleteMapping("/user/delete/{userId}")
	public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable long userId) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = userService.deleteProfile(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User not deleted ...");
		}
		return ResponseEntity.ok().body(map);
	}
	@GetMapping("/users")
	public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(defaultValue = "NOT_DELETED") String type,
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "userId") String sortBy) {
		Map<String, Object> map = new HashMap<>();
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		try {
			map = userService.getAllUsers(type,paging);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Users not Fetched ...");
		}
		return ResponseEntity.ok().body(map);
	}
	@PostMapping("/block-user/{userId}")
	public ResponseEntity<Map<String, Object>> blockUser(long userId) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = userService.blockUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User did not blocked ...");
		}
		return ResponseEntity.ok().body(map);
	}
	@PostMapping("/unblock-user/{userId}")
	public ResponseEntity<Map<String, Object>> unBlockUser(long userId) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = userService.unBlockUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User did not unblocked ...");
		}
		return ResponseEntity.ok().body(map);
	}
	
	// Implementing getUserBy Id soon because we need files also related to that user

}
