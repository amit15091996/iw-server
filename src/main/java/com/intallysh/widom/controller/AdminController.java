package com.intallysh.widom.controller;


import com.intallysh.widom.dto.FileReqDto;
import com.intallysh.widom.service.FilesDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	@Autowired
	private FilesDetailService filesDetailService;

    @PostMapping("/upload-file")
    public ResponseEntity<Map<String,Object>> uploadFile(@ModelAttribute FileReqDto fileReqDto){
    	this.filesDetailService.uploadFile(fileReqDto);

        return null;
    }

}
