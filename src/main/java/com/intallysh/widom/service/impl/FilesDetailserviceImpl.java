package com.intallysh.widom.service.impl;

import com.intallysh.widom.dto.FileReqDto;
import com.intallysh.widom.service.FilesDetailService;
import com.intallysh.widom.util.Utils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class FilesDetailserviceImpl implements FilesDetailService {

	public String filelocation = "C:/eidikoportal/";
	public String folderType = "Admin/";

	@Override
	public Map<String, Object> uploadFile(FileReqDto fileReqDto) {
		Date reportDate = fileReqDto.getReportDate();
		int reportYear = reportDate.toLocalDate().getYear();
		System.out.println(reportYear);
		String folder = filelocation + folderType + reportYear;
		if (Utils.createFolderIfNotExist(folder)) {
			for (MultipartFile f : fileReqDto.getFiles()) {
				try {
					String extension = StringUtils.getFilenameExtension(f.getOriginalFilename());
					byte[] bytes = f.getBytes();
					Path path = Paths.get(folder, UUID.randomUUID().toString() + "." + extension);
					Files.write(path, bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
