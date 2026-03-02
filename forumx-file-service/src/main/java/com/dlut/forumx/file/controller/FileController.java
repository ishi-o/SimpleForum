package com.dlut.forumx.file.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.file.FileInfoDTO;
import com.dlut.forumx.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	// ========== 前端直接调用的接口 ==========

	/**
	 * 上传头像
	 */
	@PostMapping("/avatar")
	public ResultDTO<FileInfoDTO> uploadAvatar(
			@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) { // 从请求参数获取userId

		FileInfoDTO fileInfo = fileService.uploadImage(file, "avatar", userId, true);
		return ResultDTO.success(fileInfo);
	}

	/**
	 * 上传帖子图片
	 */
	@PostMapping("/post/image")
	public ResultDTO<FileInfoDTO> uploadPostImage(
			@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) {

		FileInfoDTO fileInfo = fileService.uploadImage(file, "post", userId, true);
		return ResultDTO.success(fileInfo);
	}

	/**
	 * 获取文件访问URL
	 */
	@GetMapping("/url/{bucket}/{objectName}")
	public ResultDTO<String> getFileUrl(
			@PathVariable String bucket,
			@PathVariable String objectName) {

		String url = fileService.getFileUrl(objectName, bucket);
		return ResultDTO.success(url);
	}

	/**
	 * 下载文件
	 */
	@GetMapping("/download/{bucket}/{objectName}")
	public ResponseEntity<Resource> downloadFile(
			@PathVariable String bucket,
			@PathVariable String objectName) throws Exception {

		InputStream inputStream = fileService.downloadFile(objectName, bucket);
		InputStreamResource resource = new InputStreamResource(inputStream);

		String fileName = objectName.substring(objectName.lastIndexOf("/") + 1);
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + encodedFileName + "\"")
				.body(resource);
	}

	// ========== 内部服务调用的接口 ==========

	/**
	 * 上传临时文件
	 */
	@PostMapping("/internal/temp")
	public ResultDTO<FileInfoDTO> uploadTemp(
			@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) {

		FileInfoDTO fileInfo = fileService.uploadFile(file, "temp", userId);
		return ResultDTO.success(fileInfo);
	}

	/**
	 * 删除文件
	 */
	@DeleteMapping("/internal/{bucket}/{objectName}")
	public ResultDTO<Void> deleteFile(
			@PathVariable String bucket,
			@PathVariable String objectName,
			@RequestParam("userId") Long userId) {

		fileService.deleteFile(objectName, bucket);
		return ResultDTO.success(null);
	}

	/**
	 * 批量删除文件
	 */
	@DeleteMapping("/internal/batch")
	public ResultDTO<Void> batchDeleteFiles(
			@RequestBody List<String> objectNames,
			@RequestParam String bucket,
			@RequestParam Long userId) {

		for (String objectName : objectNames) {
			fileService.deleteFile(objectName, bucket);
		}
		return ResultDTO.success(null);
	}

	// ========== 分片上传接口（内部调用） ==========

	/**
	 * 初始化分片上传
	 */
	@PostMapping("/internal/chunk/init")
	public ResultDTO<String> initChunkUpload(
			@RequestParam String fileName,
			@RequestParam String bucket,
			@RequestParam Long userId) {

		String uploadId = fileService.initChunkUpload(fileName, bucket, userId);
		return ResultDTO.success(uploadId);
	}

	/**
	 * 上传分片
	 */
	@PostMapping("/internal/chunk/upload")
	public ResultDTO<Void> uploadChunk(
			@RequestParam String uploadId,
			@RequestParam int chunkNumber,
			@RequestBody byte[] data) {

		fileService.uploadChunk(uploadId, chunkNumber, data);
		return ResultDTO.success(null);
	}

	/**
	 * 完成分片上传
	 */
	@PostMapping("/internal/chunk/complete")
	public ResultDTO<FileInfoDTO> completeChunkUpload(
			@RequestParam String uploadId,
			@RequestParam String fileName,
			@RequestParam String bucket) {

		FileInfoDTO fileInfo = fileService.completeChunkUpload(uploadId, fileName, bucket);
		return ResultDTO.success(fileInfo);
	}
}
