package com.project.utils;

import java.net.URISyntaxException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfig {
	
	@Bean
	public AmazonS3 s3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAWOKVPDT2QEXAJB4J", "IYrUx+NKQVDilr8dm098xbNwYKdEeBhLxOgifNyr");
		return AmazonS3ClientBuilder
				.standard()
				.withRegion(Regions.US_EAST_2)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}
	
//    @Bean
//    public BasicDataSource dataSource() throws URISyntaxException {
//        String dbUrl = System.getenv("spring.datasource.url");
//        String username = System.getenv("spring.datasource.username");
//        String password = System.getenv("spring.datasource.password");
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl(dbUrl);
//        basicDataSource.setUsername(username);
//        basicDataSource.setPassword(password);
//
//        return basicDataSource;
//    }

}
