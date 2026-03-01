package com.dlut.forumx.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.content.entity.Attachment;

@Mapper
public interface AttachmentMapper {
	int insert(Attachment attachment);

	int batchInsert(@Param("list") List<Attachment> attachments);

	int deleteById(@Param("aid") Long aid);

	int deleteByPostId(@Param("postId") Long postId);

	int update(Attachment attachment);

	Attachment selectById(@Param("aid") Long aid);

	List<Attachment> selectByPostId(@Param("postId") Long postId);

	Attachment selectByObjectKey(@Param("objectKey") String objectKey);

	List<Attachment> selectByFileType(@Param("fileType") Integer fileType,
			@Param("offset") int offset,
			@Param("limit") int limit);
}
