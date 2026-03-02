package com.dlut.forumx.search.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName = "user")
public class UserDocument {

	@Id
	private Long id;

	@Field(type = FieldType.Keyword)
	private String username;

	@Field(type = FieldType.Text, analyzer = "ik_smart")
	private String nickname;

	@Field(type = FieldType.Text, analyzer = "ik_smart")
	private String bio;

	@Field(type = FieldType.Date)
	private LocalDateTime registerTime;
}
