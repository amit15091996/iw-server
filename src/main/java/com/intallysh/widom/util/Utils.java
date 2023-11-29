package com.intallysh.widom.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

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

}
