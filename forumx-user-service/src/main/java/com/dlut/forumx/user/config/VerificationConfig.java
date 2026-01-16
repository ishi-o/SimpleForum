package com.dlut.forumx.user.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.dlut.forumx.user.verification.service.EmailVerificationService;
import com.dlut.forumx.user.verification.service.SmsVerificationService;
import com.dlut.forumx.user.verification.service.VerificationService;

@Configuration
public class VerificationConfig {
	@Bean
	@Qualifier("smsVerificationService")
	public VerificationService smsVerificationService(RedisTemplate<String, String> redisTemplate,
			RabbitTemplate rabbitTemplate) {
		return new SmsVerificationService(redisTemplate, rabbitTemplate);
	}

	@Bean
	@Qualifier("emailVerificationService")
	public VerificationService emailVerificationService(RedisTemplate<String, String> redisTemplate,
			RabbitTemplate rabbitTemplate) {
		return new EmailVerificationService(redisTemplate, rabbitTemplate);
	}
}
