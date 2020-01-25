package com.project.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public interface StorageUtils {

	public static void saveImage(MultipartFile imageFile, String pathStr) throws IOException {
		byte[] bytes = imageFile.getBytes();
		Path path = Paths.get(pathStr);
		Files.write(path, bytes);
	}
}