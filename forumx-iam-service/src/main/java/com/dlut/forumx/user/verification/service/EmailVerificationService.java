package com.dlut.forumx.user.verification.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import com.dlut.forumx.common.utils.MaskUtils.MaskType;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;

public class EmailVerificationService extends AbstractVerificationService {

	public EmailVerificationService(RedisTemplate<String, String> redisTemplate, RabbitTemplate rabbitTemplate) {
		super(redisTemplate, rabbitTemplate);
	}

	@Override
	protected String getCodeKeyPrefix() {
		return "verification:code:email";
	}

	@Override
	protected String getRoutingKey() {
		return "binding.verification.email";
	}

	@Override
	protected VerificationType getMessageType() {
		return VerificationType.EMAIL;
	}

	@Override
	protected MaskType getMaskType() {
		return MaskType.EMAIL;
	}

}
