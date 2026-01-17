package com.dlut.forumx.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {
	private String accessKeyId;
	private String accessKeySecret;
	private String signName;
	private String templateCode;
	private String regionId = "cn-hangzhou";
}
