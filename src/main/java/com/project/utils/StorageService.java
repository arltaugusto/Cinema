package com.project.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.entities.Movie;

@Service
public class StorageService {
	
	@Autowired private FileStore fileStore;
	
	public void saveImage(MultipartFile imageFile, String pathStr) throws IOException {
		if(!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_PNG.getMimeType()).contains(imageFile.getContentType())) {
			throw new IllegalStateException("not supported type");
		}
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", imageFile.getContentType());
		metadata.put("Content-Lenght", String.valueOf(imageFile.getSize()));
		fileStore.save(pathStr, imageFile.getOriginalFilename(), Optional.of(metadata), imageFile.getInputStream());
	}
	
	public void deleteImage(Path path) throws IOException {
		Files.delete(path);
	}

	public byte[] downloadImage(Movie movie) {
		String path = String.format("%s/%s", BucketName.MOVIE_IMAGE.getBucketName(), movie.getMovieId());
		return fileStore.download(path, movie.getImagePath());
		
	}
}