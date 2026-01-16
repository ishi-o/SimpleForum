package com.dlut.forumx.user.verification.service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

import com.dlut.forumx.common.utils.MaskUtils;
import com.dlut.forumx.common.utils.MaskUtils.MaskType;
import com.dlut.forumx.user.verification.mq.VerificationMessage;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base service for verification code operations
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractVerificationService implements VerificationService {

	protected final RedisTemplate<String, String> redisTemplate;
	protected final RabbitTemplate rabbitTemplate;

	@Value("${verification.code-length:6}")
	protected int codeLength;

	@Value("${verification.expire-minutes:5}")
	protected int expireMinutes;

	@Value("${verification.frequency-limit-seconds:60}")
	protected int frequencyLimitSeconds;

	/**
	 * Generates a random numeric verification code
	 */
	protected String generateCode() {
		StringBuilder sb = new StringBuilder(codeLength);
		for (int i = 0; i < codeLength; i++) {
			sb.append(ThreadLocalRandom.current().nextInt(10));
		}
		return sb.toString();
	}

	/**
	 * Checks frequency limit for the target
	 */
	protected void checkFrequencyLimit(String target) {
		String freqKey = getFrequencyKey(target);
		Boolean canSend = redisTemplate.opsForValue().setIfAbsent(
				freqKey,
				"1",
				frequencyLimitSeconds,
				TimeUnit.SECONDS);

		if (Boolean.FALSE.equals(canSend)) {
			Long ttl = redisTemplate.getExpire(freqKey);
			log.warn("VERIFY-FREQ-LIMIT | Frequency limit exceeded | target={}, retryAfter={}s",
					MaskUtils.maskTarget(target, getMaskType()), ttl);
			throw new IllegalArgumentException("Request too frequent");
		}
	}

	/**
	 * Asynchronously sends verification code via message queue
	 */
	@Async
	protected void sendCode(String target, String code) {
		final String maskedTarget = MaskUtils.maskTarget(target, getMaskType());
		final VerificationType messageType = getMessageType();
		final MaskType maskType = getMaskType();

		try {
			VerificationMessage message = VerificationMessage.verificationMessage(target, code);
			String exchange = getExchangeName();
			String routingKey = getRoutingKey();

			rabbitTemplate.convertAndSend(exchange, routingKey, message, m -> {
				m.getMessageProperties().setHeader("__MessageType__", messageType);
				m.getMessageProperties().setHeader("__MaskType__", maskType);
				return m;
			});

			log.debug("VERIFY-MQ-SEND | Message dispatched | target={}, type={}",
					maskedTarget, messageType);

		} catch (Exception e) {
			log.error("VERIFY-MQ-ERROR | Failed to send message | target={}, type={}",
					maskedTarget, messageType, e);
		}
	}

	protected String getFrequencyKey(String target) {
		return "freq:" + getCodeKey(target);
	}

	protected String getCodeKey(String target) {
		return getCodeKeyPrefix() + ":" + target;
	}

	protected String getExchangeName() {
		return "exchange.verification.direct";
	}

	@Override
	public void sendCode(String target) {
		String code = generateCode();
		String codeKey = getCodeKey(target);
		checkFrequencyLimit(target);
		redisTemplate.opsForValue().set(codeKey, code, expireMinutes, TimeUnit.MINUTES);
		sendCode(target, code);
	}

	@Override
	public boolean verifyCode(String target, String code) {
		String codeKey = getCodeKey(target);
		String rcode = redisTemplate.opsForValue().get(codeKey);
		if (code.equals(rcode)) {
			redisTemplate.delete(codeKey);
			return true;
		} else {
			return false;
		}
	}

	protected abstract String getCodeKeyPrefix();

	protected abstract String getRoutingKey();

	protected abstract VerificationType getMessageType();

	protected abstract MaskType getMaskType();
}
