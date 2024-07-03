package com.intallysh.widom.service;

import java.sql.Date;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.intallysh.widom.dto.FileReqDto;

@Service
public interface FilesDetailService {

	public Map<String, Object> uploadFile(FileReqDto fileReqDto) throws Exception;

	public Map<String, Object> uploadFileByAdmin(FileReqDto fileReqDto, long userId) throws Exception;

	public Map<String, Object> getUploadedFileYears(long userId);

	public Map<String, Object> getFileByYearAndUserId(long userId, int year, Pageable paging);
	
	public Map<String, Object> getFileByYearAndDateType(long userId, Date fromDate, Date toDate, Pageable paging);

	public Map<String, Object> getFileByUserId(long userId, Pageable paging);

	public Map<String, Object> getAllFile(Pageable paging);

	public Map<String, Object> getFileDetailByTransId(String transId);

	public Map<String, Object> getFile(long fileId);
	
	public ResponseEntity<Map<String, Object>> deleteFileByTransId(String transId);
}
