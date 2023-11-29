package com.intallysh.widom.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @PostMapping("/upload-file")
    public ResponseEntity<Map<String,Object>> uploadFile(){
        return null;
    }

}
