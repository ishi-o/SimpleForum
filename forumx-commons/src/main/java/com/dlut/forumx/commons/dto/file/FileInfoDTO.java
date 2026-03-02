package com.dlut.forumx.commons.dto.file;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileInfoDTO {
	private String fileId; // 文件ID（MinIO中的对象名）
	private String fileName; // 原文件名
	private String fileUrl; // 访问URL
	private String thumbnailUrl; // 缩略图URL
	private Long fileSize; // 文件大小
	private String contentType; // 文件类型
	private LocalDateTime uploadTime; // 上传时间
}
