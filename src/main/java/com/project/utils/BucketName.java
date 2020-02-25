package com.project.utils;

public enum BucketName {
	MOVIE_IMAGE("java-cinema-project-image-upload-123");
	
	private final String bucketName;
	
	BucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public String getBucketName() {
		return bucketName;
	}
}
