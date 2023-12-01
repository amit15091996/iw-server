package com.intallysh.widom.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.intallysh.widom.exception.ResourceNotProcessedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

	public static List<Map<String, Object>> uploadFiles(List<MultipartFile> files, String folder) throws Exception {
		List<Map<String, Object>> uploadedFiles = new ArrayList<>();
		if (Utils.createFolderIfNotExist(folder)) {
			String transId = UUID.randomUUID().toString();
			for (int i = 0; i < files.size(); i++) {
				MultipartFile f = files.get(i);
				try {
					Map<String, Object> filesUploading = new HashMap<>();
					String extension = StringUtils.getFilenameExtension(f.getOriginalFilename());
					String fileId = transId;
					String fileName = fileId + "_" + i + "." + extension;
					byte[] bytes = f.getBytes();
					Path path = Paths.get(folder, fileName);
					Files.write(path, bytes);
					filesUploading.put(ConstantValues.FILE_NAME, fileName);
					filesUploading.put(ConstantValues.FOLDER, folder);
					filesUploading.put(ConstantValues.UPLOADED_DATE, new Timestamp(System.currentTimeMillis()));
					filesUploading.put(ConstantValues.FILE_ID, fileId);
					uploadedFiles.add(filesUploading);
				} catch (IOException e) {
					e.printStackTrace();
					throw new ResourceNotProcessedException("File Not Uploaded ...");
				}
			}
		}
		return uploadedFiles;
	}

	public static boolean deleteFiles(List<String> filesPath) {
		for (String filePath : filesPath) {
			try {
				Files.deleteIfExists(Paths.get(filePath));
				System.out.println("File deleted successfully");

			} catch (Exception deleteException) {
				System.err.println("Error deleting the file: " + deleteException.getMessage());
				return false;
			}
		}
		return true;
	}

	public static boolean createFolderIfNotExist(String folderPath) {
		if (isFolderExists(folderPath)) {
			System.out.println("The folder exists.");
			return true;
		} else {
			if (createFolder(folderPath)) {
				System.out.println("The folder has been created.");
				return true;
			} else {
				System.out.println("Failed to create the folder.");
			}
			return false;
		}
	};

	private static boolean isFolderExists(String folderPath) {
		Path path = Paths.get(folderPath);
		return Files.exists(path) && Files.isDirectory(path);
	}

	private static boolean createFolder(String folderPath) {
		Path path = Paths.get(folderPath);
		try {
			Files.createDirectories(path);
			return true;
		} catch (IOException e) {
			e.printStackTrace(); // Handle the exception according to your application's needs
			return false;
		}
	}

	public static Date stringToDate(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utilDate = dateFormat.parse(date);
		return new java.sql.Date(utilDate.getTime());
	}

}
