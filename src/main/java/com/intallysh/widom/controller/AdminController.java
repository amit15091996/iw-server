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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

@RestController
@PreAuthorize("hasAuthority('ADMIN_ROLE')")
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Autowired
	private FilesDetailService filesDetailService;
	
	@Autowired
	private UserService userService;
	
	
//	FIle Services Implementations Started

	
	@PostMapping("/upload-file")
	public ResponseEntity<Map<String, Object>> uploadFile(@Valid @ModelAttribute FileReqDto fileReqDto)
			throws Exception {
		return ResponseEntity.ok().body(filesDetailService.uploadFile(fileReqDto));
	}
	
	@GetMapping("/get-uploaded-file-years/{userId}")
	public ResponseEntity<Map<String, Object>> getUploadedFileYears(@PathVariable long userId)
			throws Exception {
		return ResponseEntity.ok().body(filesDetailService.getUploadedFileYears(userId));
	}
	@GetMapping("/get-filetransdetail-by-year-and-userid")
	public ResponseEntity<Map<String, Object>> getFileDetailByUserIdAndYear(@RequestParam long userId,@RequestParam int year,
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "reportDate") String sortBy)
			throws Exception {
		Pageable paging = PageRequest.of(pageNo, pageSize,Sort.by(sortBy).descending());
		return ResponseEntity.ok().body(filesDetailService.getFileByYearAndUserId(userId, year,paging));
	}
	
	@GetMapping("/get-filedetail-by-transid/{transId}")
	public ResponseEntity<Map<String, Object>> getFileDetailByTransId(@PathVariable String transId)
			throws Exception {
//		Pageable paging = PageRequest.of(pageNo, pageSize,Sort.by(sortBy).descending());
		return ResponseEntity.ok().body(filesDetailService.getFileDetailByTransId(transId));
	}
	
	@GetMapping(path = "/get-file/{fileId}")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable long fileId) throws Exception {
	    Map<String, Object> file = filesDetailService.getFile(fileId);
	    InputStream fileData = (InputStream) file.get("fileData");
	    String fileName = (String) file.get("fileName");
	    System.out.println("----- File Name :  --------" + fileName);

	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\""); // Wrap filename in quotes

	    // Set Content-Type based on file extension or any other condition
	    String contentType = determineContentType(fileName);
	    headers.add(HttpHeaders.CONTENT_TYPE, contentType);

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(new InputStreamResource(fileData));
	}



	private String determineContentType(String fileName) {
	    // You can implement logic to determine Content-Type based on the file extension
	    // For example, check if the file name ends with ".docx", ".pdf", etc.
	    if (fileName.endsWith(".docx")) {
	        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	    } else if (fileName.endsWith(".pdf")) {
	        return "application/pdf";
	    }
	    else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
	        return "image/jpeg";
	    }else if (fileName.endsWith(".png")) {
	        return "image/png";
	    }
	    // Add more conditions as needed

	    // Default to a generic content type if not determined
	    return "application/octet-stream";
	}

//	File Services Implementations Ended
	
// User Profile Related Service Started
	
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
            @RequestParam(defaultValue = "userId") String sortBy
            ) {
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
	public ResponseEntity<Map<String, Object>> blockUser(@PathVariable long userId) {
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
	public ResponseEntity<Map<String, Object>> unBlockUser(@PathVariable long userId) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = userService.unBlockUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User did not unblocked ...");
		}
		return ResponseEntity.ok().body(map);
	}
	
//	User Profile Related Service Ended
	
	// Implementing getUserBy Id soon because we need files also related to that user

}
