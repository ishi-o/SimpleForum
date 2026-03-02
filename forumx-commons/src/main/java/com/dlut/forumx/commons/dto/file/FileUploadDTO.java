package com.dlut.forumx.commons.dto.file;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FileUploadDTO {
	private MultipartFile file;
	private String type;
	private Long userId;
	private Boolean createThumbnail;
}
