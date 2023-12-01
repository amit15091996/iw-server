package com.intallysh.widom.service.impl;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.FileReqDto;
import com.intallysh.widom.entity.FilesDetail;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.FilesDetailRepo;
import com.intallysh.widom.service.FilesDetailService;
import com.intallysh.widom.util.ConstantValues;
import com.intallysh.widom.util.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

	public String fileLocation = "C:/intallysh-wisdom/";

	@Autowired
	private FilesDetailRepo filesDetailRepo;

	private User getCurrentUser() throws AuthenticationException {
		return SecurityUtil.getCurrentUserDetails();
	}

	@Override
	public Map<String, Object> uploadFile(FileReqDto fileReqDto) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Date reportDate = Utils.stringToDate(fileReqDto.getReportDate());
		int reportYear = reportDate.toLocalDate().getYear();
		String folder = fileLocation + getFolderType() + reportYear;
		List<Map<String, Object>> uploadFiles = Utils.uploadFiles(fileReqDto.getFiles(), folder);
		List<FilesDetail> fileDetailList = new ArrayList<>();
		List<String> filesPath = new ArrayList<>();
		for (Map<String, Object> file : uploadFiles) {
			String fileName = (String) file.get(ConstantValues.FILE_NAME);
			String folderName = (String) file.get(ConstantValues.FOLDER);
			Timestamp uploadedDate = (Timestamp) file.get(ConstantValues.UPLOADED_DATE);
			String fileId = (String) file.get(ConstantValues.FILE_ID);

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

			fileDetailList.add(detail);
		}
		try {
			List<FilesDetail> savedFiles = filesDetailRepo.saveAll(fileDetailList);

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

}
