package com.dlut.forumx.user.verification.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dlut.forumx.common.utils.MaskUtils.MaskType;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsVerificationService extends AbstractVerificationService {

	public SmsVerificationService(RedisTemplate<String, String> redisTemplate, RabbitTemplate rabbitTemplate) {
		super(redisTemplate, rabbitTemplate);
	}

	@Override
	protected String getCodeKeyPrefix() {
		return "verification:code:sms";
	}

	@Override
	protected String getRoutingKey() {
		return "binding.verification.sms";
	}

	@Override
	protected VerificationType getMessageType() {
		return VerificationType.SMS;
	}

	@Override
	protected MaskType getMaskType() {
		return MaskType.PHONENUMBER;
	}

}
