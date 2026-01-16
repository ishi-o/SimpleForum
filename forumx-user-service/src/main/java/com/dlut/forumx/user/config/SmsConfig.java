package com.dlut.forumx.user.config;

import org.springframework.context.annotation.Bean;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;

public class SmsConfig {

	@Bean
	public IAcsClient smsClient(SmsProperties properties) {
		return new DefaultAcsClient(DefaultProfile.getProfile(
				properties.getRegionId(),
				properties.getAccessKeyId(),
				properties.getAccessKeySecret()));
	}
}
