package com.intallysh.widom.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.intallysh.widom.dto.FileReqDto;

@Service
public interface FilesDetailService {

	public Map<String, Object> uploadFile(FileReqDto fileReqDto) throws Exception;

	public Map<String, Object> getUploadedFileYears(long userId);

	public Map<String, Object> getFileByYearAndUserId(long userId, int year, Pageable paging);

	public Map<String, Object> getFileDetailByTransId(String transId);
	
	public Map<String, Object> getFile(long fileId);
}
