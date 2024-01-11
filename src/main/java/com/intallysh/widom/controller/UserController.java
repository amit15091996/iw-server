package com.intallysh.widom.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.ChangePasswordDto;
import com.intallysh.widom.dto.FileReqDto;
import com.intallysh.widom.dto.UpdateUserReqDto;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.exception.ForbiddenException;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.service.FilesDetailService;
import com.intallysh.widom.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FilesDetailService filesDetailService;

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

	@GetMapping("/{userName}")
	public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String userName)
			throws AuthenticationException {
		return ResponseEntity.ok().body(this.userService.getUserByUserName(userName));
	}
	
	
//	User File related implementations
	
	@PostMapping("/upload-file")
	public ResponseEntity<Map<String, Object>> uploadFile(@Valid @ModelAttribute FileReqDto fileReqDto)
			throws Exception {
		return ResponseEntity.ok().body(filesDetailService.uploadFile(fileReqDto));
	}
	
	@GetMapping("/get-uploaded-file-years")
	public ResponseEntity<Map<String, Object>> getUploadedFileYears()
			throws Exception {
		long userId = SecurityUtil.getCurrentUserDetails().getUserId();
		return ResponseEntity.ok().body(filesDetailService.getUploadedFileYears(userId));
	}
	@GetMapping("/get-filetransdetail-by-year-and-userid")
	public ResponseEntity<Map<String, Object>> getFileDetailByUserIdAndYear(@RequestParam int year,
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "reportDate") String sortBy)
			throws Exception {
		long userId = SecurityUtil.getCurrentUserDetails().getUserId();
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
	
	@PostMapping("/change-password")
	private ResponseEntity<Map<String, Object>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto){
		try {
			long userId = SecurityUtil.getCurrentUserDetails().getUserId();
			return ResponseEntity.ok().body(this.userService.ChangePassword(userId, changePasswordDto));
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new ResourceNotProcessedException("Password not changed");
	}
	
}
