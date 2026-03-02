package com.dlut.forumx.search.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
@Document(indexName = "post")
public class PostDocument {

	@Id
	private Long id;

	@Field(type = FieldType.Long)
	private Long userId;

	@Field(type = FieldType.Keyword)
	private String username;

	@Field(type = FieldType.Text, analyzer = "ik_smart")
	private String nickname;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String title;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String content;

	@Field(type = FieldType.Keyword)
	private List<String> tags;

	@Field(type = FieldType.Date)
	private LocalDateTime createTime;
}
