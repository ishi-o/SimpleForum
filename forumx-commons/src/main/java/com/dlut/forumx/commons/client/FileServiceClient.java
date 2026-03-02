package com.dlut.forumx.commons.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.file.FileInfoDTO;

@FeignClient(name = "file-service", path = "/files/internal")
public interface FileServiceClient {

	/**
	 * 上传临时文件
	 */
	@PostMapping(value = "/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResultDTO<FileInfoDTO> uploadTemp(
			@RequestPart("file") MultipartFile file,
			@RequestParam("userId") Long userId);

	/**
	 * 删除文件
	 */
	@DeleteMapping("/{bucket}/{objectName}")
	ResultDTO<Void> deleteFile(
			@PathVariable String bucket,
			@PathVariable String objectName,
			@RequestParam("userId") Long userId);

	/**
	 * 批量删除文件
	 */
	@DeleteMapping("/batch")
	ResultDTO<Void> batchDeleteFiles(
			@RequestBody List<String> objectNames,
			@RequestParam String bucket,
			@RequestParam Long userId);

	/**
	 * 获取文件信息
	 */
	@GetMapping("/info/{bucket}/{objectName}")
	ResultDTO<FileInfoDTO> getFileInfo(
			@PathVariable String bucket,
			@PathVariable String objectName);

	/**
	 * 检查文件是否存在
	 */
	@GetMapping("/exists/{bucket}/{objectName}")
	ResultDTO<Boolean> fileExists(
			@PathVariable String bucket,
			@PathVariable String objectName);

	/**
	 * 初始化分片上传
	 */
	@PostMapping("/chunk/init")
	ResultDTO<String> initChunkUpload(
			@RequestParam String fileName,
			@RequestParam String bucket,
			@RequestParam Long userId);

	/**
	 * 上传分片
	 */
	@PostMapping("/chunk/upload")
	ResultDTO<Void> uploadChunk(
			@RequestParam String uploadId,
			@RequestParam int chunkNumber,
			@RequestBody byte[] data);

	/**
	 * 完成分片上传
	 */
	@PostMapping("/chunk/complete")
	ResultDTO<FileInfoDTO> completeChunkUpload(
			@RequestParam String uploadId,
			@RequestParam String fileName,
			@RequestParam String bucket);
}
