package com.intallysh.widom.service;

import com.intallysh.widom.dto.FileReqDto;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface FilesDetailService {

	public Map<String, Object> uploadFile(FileReqDto fileReqDto) throws Exception;

	public Map<String, Object> getUploadedFileYears(long userId);

	public Map<String, Object> getFileByYearAndUserId(long userId, int year, Pageable paging);

	public Map<String, Object> getFileDetailByTransId(String transId);
	
	public Map<String, Object> getFile(long fileId);
}
