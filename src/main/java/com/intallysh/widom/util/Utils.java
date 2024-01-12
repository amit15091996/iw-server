package com.intallysh.widom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.intallysh.widom.exception.ResourceNotProcessedException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Utils {

	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL = "!@#$&";

	@Value("${spring.mail.username}")
	private String sender;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static List<Map<String, Object>> uploadFiles(List<MultipartFile> files, String folder) throws Exception {
		List<Map<String, Object>> uploadedFiles = new ArrayList<>();
		if (Utils.createFolderIfNotExist(folder)) {
			String transId = UUID.randomUUID().toString();
			for (int i = 0; i < files.size(); i++) {
				MultipartFile f = files.get(i);
				try {
					Map<String, Object> filesUploading = new HashMap<>();
					String extension = StringUtils.getFilenameExtension(f.getOriginalFilename());
					if (extension == null || extension == "") {
						System.out.println("Invalid File type and file ");
						continue;
					}
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

	public static String generateRandomPassword(int length) {
		String characters = UPPER + LOWER + DIGITS + SPECIAL;
		Random random = new SecureRandom();
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			password.append(characters.charAt(randomIndex));
		}
		return password.toString();
	}
	
	public  boolean sendSimpleMail(String recieverEmail, String subject ,  String bodyText) {
		try {
			// Creating a simple mail message
			MimeMessage message = javaMailSender.createMimeMessage();
			// Setting up necessary details

			message.setFrom(sender);
			message.setRecipients(MimeMessage.RecipientType.TO, recieverEmail);
			message.setSubject(subject);

			message.setContent(bodyText, "text/html; charset=utf-8");
			javaMailSender.send(message);
			this.logger.info("<<<<<<<<<<<<<<<<<<< Mail Sent >>>>>>>>>>>>>>>>>>>>");
			return true;
		} catch (MessagingException | MailException e) {
			this.logger.info("<<<<<<<<<<<<<<<<<<< Mail not Sent >>>>>>>>>>>>>>>>>>>>");
			throw new ResourceNotProcessedException(e.getMessage());
		} 
		

	}
	
	public static String convertToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] bytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
	

}
