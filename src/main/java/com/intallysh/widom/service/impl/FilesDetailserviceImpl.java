package com.intallysh.widom.service.impl;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.FileReqDto;
import com.intallysh.widom.entity.FileTransDetails;
import com.intallysh.widom.entity.FilesDetail;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.FileTransDetailsRepo;
import com.intallysh.widom.repo.FilesDetailRepo;
import com.intallysh.widom.service.FilesDetailService;
import com.intallysh.widom.util.ConstantValues;
import com.intallysh.widom.util.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

@Service
public class FilesDetailserviceImpl implements FilesDetailService {

	public String fileLocation = ConstantValues.FILE_LOCATION;

	@Autowired
	private FilesDetailRepo filesDetailRepo;

	@Autowired
	private FileTransDetailsRepo fileTransDetailsRepo;

	private User getCurrentUser() throws AuthenticationException {
		return SecurityUtil.getCurrentUserDetails();
	}

	@Override
	public Map<String, Object> uploadFile(FileReqDto fileReqDto) throws Exception {
		if (fileReqDto.getFiles().size() > 10)
			throw new ResourceNotProcessedException("You can upload 10 files at a time");
		Map<String, Object> map = new HashMap<>();
		Date reportDate = Utils.stringToDate(fileReqDto.getReportDate());
		int reportYear = reportDate.toLocalDate().getYear();
		String folder = fileLocation + getFolderType() + reportYear + "/" + fileReqDto.getFileType();
		List<Map<String, Object>> uploadFiles = Utils.uploadFiles(fileReqDto.getFiles(), folder);
		List<FilesDetail> fileDetailList = new ArrayList<>();
		List<String> filesPath = new ArrayList<>();
		for (Map<String, Object> file : uploadFiles) {
			String fileName = (String) file.get(ConstantValues.FILE_NAME);
			String folderName = (String) file.get(ConstantValues.FOLDER);
			Timestamp uploadedDate = (Timestamp) file.get(ConstantValues.UPLOADED_DATE);
			String fileId = (String) file.get(ConstantValues.FILE_ID);
			String fileDesc = fileReqDto.getFileDescription();
			String filePath = folderName + "/" + fileName;
			filesPath.add(filePath);

			FilesDetail detail = new FilesDetail();
			detail.setTransId(fileId);
			detail.setFileExtension(StringUtils.getFilenameExtension(fileName));
			detail.setFileLocation(folderName);
			detail.setFileName(fileName);
			detail.setFileStoredTime(uploadedDate);
			detail.setFileType(fileReqDto.getFileType());
			detail.setReportDate(reportDate);
			detail.setUserId(getCurrentUser().getUserId());
			detail.setModifiedBy(getCurrentUser().getUserId());
			detail.setModifiedOn(new Timestamp(System.currentTimeMillis()));
			detail.setFileDescription(fileDesc);
			detail.setYear(reportYear);
			fileDetailList.add(detail);
		}
		try {
			List<FilesDetail> savedFiles = filesDetailRepo.saveAll(fileDetailList);
			map.put("result", savedFiles);
			if (savedFiles.size() <= 0) {
				Utils.deleteFiles(filesPath);
				throw new ResourceNotProcessedException("File not uploaded ...");
			}
		} catch (Exception e) {
			System.err.println("Error occurred: " + e.getMessage());
			Utils.deleteFiles(filesPath);
			throw new ResourceNotProcessedException("File not uploaded ...");
		}
		map.put("status", "Success");
		map.put("message", "File Uploaded Successfully");

		return map;
	}

	private String getFolderType() throws AuthenticationException {
		User currentUser = getCurrentUser();
		Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
		List<? extends GrantedAuthority> authoritiesList = new ArrayList<>(authorities);
		System.out.println(authoritiesList);
		if (containsAdminRole(authoritiesList)) {
			return "Admin/";
		} else {
			return currentUser.getUserId() + "_" + currentUser.getName() + "/";
		}
	}

	private static boolean containsAdminRole(List<? extends GrantedAuthority> authoritiesList) {
		// Check if the "ADMIN_ROLE" authority is present in the list
		for (GrantedAuthority authority : authoritiesList) {
			if ("ADMIN_ROLE".equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> getUploadedFileYears(long userId) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Long> uploadedFileYearByUserId = filesDetailRepo.getUploadedFileYearByUserId(userId);
			if (uploadedFileYearByUserId.size() > 0) {
				map.put("message", "Data Fetched Successfully ...");
				map.put("years", uploadedFileYearByUserId);
			} else {
				map.put("message", "Data not available ...");
				map.put("years", uploadedFileYearByUserId);
			}
		} catch (Exception e) {
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}

		map.put("status", "Success");
		return map;
	}

	@Override
	public Map<String, Object> getFileByYearAndUserId(long userId, int year, Pageable paging) {
		Map<String, Object> map = new HashMap<>();
		try {
			Page<FileTransDetails> fileDetails = fileTransDetailsRepo.findByUserIdAndYear(userId, year, paging);
			if (fileDetails.hasContent()) {
				map.put("message", "Data Fetched Successfully ...");
				map.put("fileTransDetails", fileDetails.getContent());
				System.out.println("---- "+map.put("fileTransDetails", fileDetails));
			} else {
				map.put("message", "Data not available ...");
				map.put("fileTransDetails", new ArrayList<>());
			}
			map.put("totalPages", fileDetails.getTotalPages());
			map.put("totalResults", fileDetails.getTotalElements());
			map.put("currentPage", fileDetails.getNumber());
			map.put("noOfElements", fileDetails.getNumberOfElements());
			map.put("isLastPage", fileDetails.isLast());
			map.put("isFirstPage", fileDetails.isFirst());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}
		map.put("status", "Success");
		return map;
	}

	@Override
	public Map<String, Object> getFileDetailByTransId(String transId) {

		Map<String, Object> map = new HashMap<>();
		try {
			List<FilesDetail> findByTransId = this.filesDetailRepo.findByTransId(transId);
			if (findByTransId.size() > 0) {
				map.put("message", "Data Fetched Successfully ...");
				map.put("fileDetails", findByTransId);
			} else {
				map.put("message", "Data not available ...");
				map.put("fileDetails", findByTransId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}
		map.put("status", "Success");
		return map;
	}

	@Override
	public Map<String, Object> getFile(long fileId) {
		Map<String, Object> map = new HashMap<>();
		FilesDetail filesDetail = this.filesDetailRepo.findById(fileId)
				.orElseThrow(() -> new ResourceNotProcessedException("File is not Available"));
		String file = filesDetail.getFileLocation() + "/" + filesDetail.getFileName();
		System.out.println("file ------ : " + file);
		map.put("fileName", filesDetail.getFileName());
		try {
			InputStream in = new FileInputStream(file);
			map.put("fileData", in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("------- FIle not found ------");
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllFile( Pageable paging) {
		Map<String, Object> map = new HashMap<>();
		try {
			Page<FileTransDetails> fileDetails = fileTransDetailsRepo.findAll(paging);
			if (fileDetails.hasContent()) {
				map.put("message", "Data Fetched Successfully ...");
				map.put("fileTransDetails", fileDetails.getContent());
				System.out.println("---- "+map.put("fileTransDetails", fileDetails));
			} else {
				map.put("message", "Data not available ...");
				map.put("fileTransDetails", new ArrayList<>());
			}
			map.put("totalPages", fileDetails.getTotalPages());
			map.put("totalResults", fileDetails.getTotalElements());
			map.put("currentPage", fileDetails.getNumber());
			map.put("noOfElements", fileDetails.getNumberOfElements());
			map.put("isLastPage", fileDetails.isLast());
			map.put("isFirstPage", fileDetails.isFirst());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}
		map.put("status", "Success");
		return map;
	}

}
