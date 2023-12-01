package com.intallysh.widom.service;

import com.intallysh.widom.dto.FileReqDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface FilesDetailService {

    public Map<String,Object> uploadFile(FileReqDto fileReqDto) throws Exception;


}
