package com.dlut.forumx.user.verification.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.dlut.forumx.common.utils.MaskUtils;
import com.dlut.forumx.common.utils.MaskUtils.MaskType;
import com.dlut.forumx.user.verification.mq.VerificationMessage;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationConsumer {
	private final JavaMailSender emailSender;

	@RabbitListener(queues = "queue.verification.email")
	public void handleSmsVerificationMessage(
			@Payload VerificationMessage message,
			@Header("__MessageType__") VerificationType messageType,
			@Header("__MaskType__") MaskType maskType) {
		final String traceId = message.getMessageId() != null ? message.getMessageId().substring(0, 8) : "N/A";
		final String maskedTarget = MaskUtils.maskTarget(message.getTarget(), maskType);
		log.info("VEMAIL-RECV | 接收邮箱发送任务 | traceId={}, target={}, type={}",
				traceId, maskedTarget, VerificationType.SMS);

		try {
			long startTime = System.currentTimeMillis();
			MimeMessage email = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(email);
			helper.setTo(message.getTarget());
			helper.setSubject("Forumx: 验证码");
			helper.setText("您的验证码是: " + message.getCode());
			long costTime = System.currentTimeMillis() - startTime;
			log.info("VSMS-SUCC | 短信发送成功 | traceId={}, target={}, cost={}ms",
					traceId, maskedTarget, costTime);
		} catch (IllegalArgumentException e) {
			log.warn("VSMS-REJECT | 消息格式非法，拒绝处理 | traceId={}, target={}, reason={}",
					traceId, maskedTarget, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("VSMS-ERROR | 短信发送系统异常 | traceId={}, target={}",
					traceId, maskedTarget, e);
			log.warn("VSMS-FAIL | 短信服务返回失败 | traceId={}, target={}",
					traceId, maskedTarget);
			throw new RuntimeException("SMS_SYSTEM_ERROR", e);
		}
	}
}
