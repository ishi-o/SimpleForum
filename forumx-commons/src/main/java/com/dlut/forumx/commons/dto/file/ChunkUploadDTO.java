package com.dlut.forumx.commons.dto.file;

import lombok.Data;

@Data
public class ChunkUploadDTO {
	private String uploadId; // 上传ID
	private Integer chunkNumber; // 当前分片序号
	private Integer totalChunks; // 总分片数
	private Long totalSize; // 文件总大小
	private String fileName; // 文件名
	private byte[] data; // 分片数据
}
