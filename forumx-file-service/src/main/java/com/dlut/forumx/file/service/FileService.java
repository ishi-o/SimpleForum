package com.dlut.forumx.file.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.dlut.forumx.commons.dto.file.FileInfoDTO;

public interface FileService {

	/**
	 * 上传文件
	 */
	FileInfoDTO uploadFile(MultipartFile file, String bucket, Long userId);

	/**
	 * 上传图片并生成缩略图
	 */
	FileInfoDTO uploadImage(MultipartFile file, String bucket, Long userId, boolean createThumbnail);

	/**
	 * 删除文件
	 */
	void deleteFile(String objectName, String bucket);

	/**
	 * 获取文件访问URL
	 */
	String getFileUrl(String objectName, String bucket);

	/**
	 * 下载文件
	 */
	InputStream downloadFile(String objectName, String bucket);

	/**
	 * 初始化分片上传
	 */
	String initChunkUpload(String fileName, String bucket, Long userId);

	/**
	 * 上传分片
	 */
	void uploadChunk(String uploadId, int chunkNumber, byte[] data);

	/**
	 * 合并分片
	 */
	FileInfoDTO completeChunkUpload(String uploadId, String fileName, String bucket);
}
