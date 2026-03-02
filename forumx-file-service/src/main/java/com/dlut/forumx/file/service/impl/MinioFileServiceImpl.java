package com.dlut.forumx.file.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dlut.forumx.commons.dto.file.FileInfoDTO;
import com.dlut.forumx.file.config.MinioConfig;
import com.dlut.forumx.file.service.FileService;

import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileServiceImpl implements FileService {

	private final MinioClient minioClient;
	private final MinioConfig minioConfig;

	// 临时存储分片上传信息（实际应该用Redis）
	private final Map<String, ChunkUploadInfo> chunkUploadMap = new HashMap<>();

	@Override
	public FileInfoDTO uploadFile(MultipartFile file, String bucket, Long userId) {
		try {
			// 1. 生成文件名
			String originalFilename = file.getOriginalFilename();
			String extension = getFileExtension(originalFilename);
			String objectName = generateObjectName(userId, extension);

			// 2. 确保bucket存在
			ensureBucketExists(bucket);

			// 3. 上传文件
			PutObjectArgs args = PutObjectArgs.builder()
					.bucket(bucket)
					.object(objectName)
					.stream(file.getInputStream(), file.getSize(), -1)
					.contentType(file.getContentType())
					.build();

			minioClient.putObject(args);

			// 4. 构建返回信息
			return buildFileInfoDTO(objectName, originalFilename, bucket, file.getSize(), file.getContentType());

		} catch (Exception e) {
			log.error("上传文件失败", e);
			throw new RuntimeException("上传文件失败: " + e.getMessage());
		}
	}

	@Override
	public FileInfoDTO uploadImage(MultipartFile file, String bucket, Long userId, boolean createThumbnail) {
		try {
			// 1. 上传原图
			FileInfoDTO fileInfo = uploadFile(file, bucket, userId);

			// 2. 生成缩略图
			if (createThumbnail) {
				String thumbnailName = fileInfo.getFileId().replace(".", "_thumb.");
				generateThumbnail(file, bucket, thumbnailName);
				fileInfo.setThumbnailUrl(getFileUrl(thumbnailName, bucket));
			}

			return fileInfo;

		} catch (Exception e) {
			log.error("上传图片失败", e);
			throw new RuntimeException("上传图片失败: " + e.getMessage());
		}
	}

	@Override
	public void deleteFile(String objectName, String bucket) {
		try {
			RemoveObjectArgs args = RemoveObjectArgs.builder()
					.bucket(bucket)
					.object(objectName)
					.build();
			minioClient.removeObject(args);

			// 同时删除缩略图
			if (objectName.contains(".")) {
				String thumbnailName = objectName.replace(".", "_thumb.");
				try {
					minioClient.removeObject(RemoveObjectArgs.builder()
							.bucket(bucket)
							.object(thumbnailName)
							.build());
				} catch (Exception e) {
					// 缩略图不存在，忽略
				}
			}

		} catch (Exception e) {
			log.error("删除文件失败", e);
			throw new RuntimeException("删除文件失败: " + e.getMessage());
		}
	}

	@Override
	public String getFileUrl(String objectName, String bucket) {
		try {
			// 生成7天有效的访问链接
			GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
					.method(Method.GET)
					.bucket(bucket)
					.object(objectName)
					.expiry(7, TimeUnit.DAYS)
					.build();

			return minioClient.getPresignedObjectUrl(args);
		} catch (Exception e) {
			log.error("获取文件URL失败", e);
			// 如果生成预签名URL失败，返回公开访问URL
			return String.format("%s/%s/%s", minioConfig.getPublicUrl(), bucket, objectName);
		}
	}

	@Override
	public InputStream downloadFile(String objectName, String bucket) {
		try {
			GetObjectArgs args = GetObjectArgs.builder()
					.bucket(bucket)
					.object(objectName)
					.build();
			return minioClient.getObject(args);
		} catch (Exception e) {
			log.error("下载文件失败", e);
			throw new RuntimeException("下载文件失败: " + e.getMessage());
		}
	}

	@Override
	public String initChunkUpload(String fileName, String bucket, Long userId) {
		String uploadId = UUID.randomUUID().toString();
		String objectName = generateObjectName(userId, getFileExtension(fileName));

		ChunkUploadInfo info = new ChunkUploadInfo();
		info.setUploadId(uploadId);
		info.setBucket(bucket);
		info.setObjectName(objectName);
		info.setFileName(fileName);
		info.setUserId(userId);
		info.setChunks(new HashMap<>());

		chunkUploadMap.put(uploadId, info);

		return uploadId;
	}

	@Override
	public void uploadChunk(String uploadId, int chunkNumber, byte[] data) {
		ChunkUploadInfo info = chunkUploadMap.get(uploadId);
		if (info == null) {
			throw new RuntimeException("上传会话不存在");
		}

		try {
			// 上传分片到临时目录
			String chunkName = info.getObjectName() + ".part" + chunkNumber;
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

			PutObjectArgs args = PutObjectArgs.builder()
					.bucket(minioConfig.getBucket().getTemp())
					.object(chunkName)
					.stream(inputStream, data.length, -1)
					.build();

			minioClient.putObject(args);
			info.getChunks().put(chunkNumber, chunkName);

		} catch (Exception e) {
			log.error("上传分片失败", e);
			throw new RuntimeException("上传分片失败: " + e.getMessage());
		}
	}

	@Override
	public FileInfoDTO completeChunkUpload(String uploadId, String fileName, String bucket) {
		ChunkUploadInfo info = chunkUploadMap.get(uploadId);
		if (info == null) {
			throw new RuntimeException("上传会话不存在");
		}

		try {
			// 合并分片
			List<ComposeSource> sources = info.getChunks().entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.map(entry -> ComposeSource.builder()
							.bucket(minioConfig.getBucket().getTemp())
							.object(entry.getValue())
							.build())
					.collect(Collectors.toList());

			ComposeObjectArgs args = ComposeObjectArgs.builder()
					.bucket(bucket)
					.object(info.getObjectName())
					.sources(sources)
					.build();

			minioClient.composeObject(args);

			// 清理临时文件
			for (String chunkName : info.getChunks().values()) {
				minioClient.removeObject(RemoveObjectArgs.builder()
						.bucket(minioConfig.getBucket().getTemp())
						.object(chunkName)
						.build());
			}

			// 获取文件信息
			StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
					.bucket(bucket)
					.object(info.getObjectName())
					.build());

			chunkUploadMap.remove(uploadId);

			return buildFileInfoDTO(info.getObjectName(), fileName, bucket,
					stat.size(), stat.contentType());

		} catch (Exception e) {
			log.error("合并分片失败", e);
			throw new RuntimeException("合并分片失败: " + e.getMessage());
		}
	}

	// ================== 私有方法 ==================

	private String generateObjectName(Long userId, String extension) {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return String.format("%d/%s/%s%s", userId, date, uuid, extension);
	}

	private String getFileExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf("."));
	}

	private void ensureBucketExists(String bucket) throws Exception {
		boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
		if (!exists) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
		}
	}

	private void generateThumbnail(MultipartFile file, String bucket, String thumbnailName) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Thumbnails.of(file.getInputStream())
				.size(200, 200)
				.toOutputStream(outputStream);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		try {
			PutObjectArgs args = PutObjectArgs.builder()
					.bucket(bucket)
					.object(thumbnailName)
					.stream(inputStream, outputStream.size(), -1)
					.contentType("image/jpeg")
					.build();
			minioClient.putObject(args);
		} catch (Exception e) {
			log.error("生成缩略图失败", e);
		}
	}

	private FileInfoDTO buildFileInfoDTO(String objectName, String originalFilename,
			String bucket, long size, String contentType) {
		FileInfoDTO dto = new FileInfoDTO();
		dto.setFileId(objectName);
		dto.setFileName(originalFilename);
		dto.setFileUrl(getFileUrl(objectName, bucket));
		dto.setFileSize(size);
		dto.setContentType(contentType);
		dto.setUploadTime(LocalDateTime.now());
		return dto;
	}

	@Data
	private static class ChunkUploadInfo {
		private String uploadId;
		private String bucket;
		private String objectName;
		private String fileName;
		private Long userId;
		private Map<Integer, String> chunks;
	}
}
