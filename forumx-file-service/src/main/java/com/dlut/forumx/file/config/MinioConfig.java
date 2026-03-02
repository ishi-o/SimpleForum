package com.dlut.forumx.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String publicUrl;

	private Bucket bucket = new Bucket();

	@Data
	public static class Bucket {
		private String avatar;
		private String post;
		private String temp;
	}

	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
				.endpoint(endpoint)
				.credentials(accessKey, secretKey)
				.build();
	}
}
