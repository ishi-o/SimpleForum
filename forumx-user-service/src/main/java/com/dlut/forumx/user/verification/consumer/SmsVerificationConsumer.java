package com.dlut.forumx.user.verification.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.dlut.forumx.common.utils.MaskUtils;
import com.dlut.forumx.common.utils.MaskUtils.MaskType;
import com.dlut.forumx.user.config.SmsProperties;
import com.dlut.forumx.user.verification.mq.VerificationMessage;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SmsVerificationConsumer {

	private final IAcsClient smsClient;
	private final SmsProperties smsProperties;

	@RabbitListener(queues = "queue.verification.sms")
	public void handleSmsVerificationMessage(
			@Payload VerificationMessage message,
			@Header("__MessageType__") VerificationType messageType,
			@Header("__MaskType__") MaskType maskType) {
		final String traceId = message.getMessageId() != null ? message.getMessageId().substring(0, 8) : "N/A";
		final String maskedTarget = MaskUtils.maskTarget(message.getTarget(), maskType);

		log.info("VSMS-RECV | 接收短信发送任务 | traceId={}, target={}, type={}",
				traceId, maskedTarget, VerificationType.SMS);

		try {
			long startTime = System.currentTimeMillis();
			boolean success = sendSms(message.getTarget(), message.getCode(), maskedTarget);
			long costTime = System.currentTimeMillis() - startTime;

			if (success) {
				log.info("VSMS-SUCC | 短信发送成功 | traceId={}, target={}, cost={}ms",
						traceId, maskedTarget, costTime);
			} else {
				log.warn("VSMS-FAIL | 短信服务返回失败 | traceId={}, target={}, cost={}ms",
						traceId, maskedTarget, costTime);
				throw new RuntimeException("SMS_SERVICE_RETURN_FAIL");
			}

		} catch (IllegalArgumentException e) {
			log.warn("VSMS-REJECT | 消息格式非法，拒绝处理 | traceId={}, target={}, reason={}",
					traceId, maskedTarget, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("VSMS-ERROR | 短信发送系统异常 | traceId={}, target={}",
					traceId, maskedTarget, e);
			throw new RuntimeException("SMS_SYSTEM_ERROR", e);
		}
	}

	/**
	 * Send sms code
	 */
	private boolean sendSms(String target, String code, String maskedTarget) {
		long startTime = System.currentTimeMillis();

		try {
			log.debug("VSMS-API | 调用短信网关开始 | target={}", maskedTarget);

			SendSmsRequest req = new SendSmsRequest();
			req.setPhoneNumbers(target);
			req.setSignName(smsProperties.getSignName());
			req.setTemplateCode(smsProperties.getTemplateCode());
			req.setTemplateParam("{\"code\":\"" + code + "\"}");
			SendSmsResponse resp = smsClient.getAcsResponse(req);
			log.info("VSMS-API | 短信网关响应 | target={}, vendorCode={}, vendorMsg={}",
					maskedTarget, resp.getCode(), resp.getMessage());

			boolean success = "OK".equalsIgnoreCase(code);
			long cost = System.currentTimeMillis() - startTime;
			log.debug("VSMS-API | 调用短信网关完成 | target={}, result={}, cost={}ms",
					maskedTarget, success ? "SUCCESS" : "FAILED", cost);

			return success;
		} catch (Exception e) {
			log.error("VSMS-API-ERR | 短信网关调用异常 | target={}, cost={}ms",
					maskedTarget, System.currentTimeMillis() - startTime, e);
			return false;
		}
	}

}
