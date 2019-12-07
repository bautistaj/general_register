package com.collect.api.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	public static String saveFile(MultipartFile file, String path) throws IOException {
		String name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
		Path filePath = Paths.get(path).resolve(name).toAbsolutePath();
		
		Files.copy(file.getInputStream(), filePath);
		
		return name;
	}
	
	public static void deleteFile(String path, String fileName) {
		Path previusFilePath = Paths.get(path).resolve(fileName).toAbsolutePath();
		File previusFile = previusFilePath.toFile();

		if (previusFile.exists()) {
			previusFile.delete();
		}
	}
}
