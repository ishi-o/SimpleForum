package com.dlut.forumx.user.verification.mq;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 
 */
@Data
@Builder
public class VerificationMessage {
	private String messageId;
	private String target;
	private String code;
	private Long timestamp;

	public static VerificationMessage verificationMessage(String target, String code) {
		return VerificationMessage.builder()
				.messageId(generateMessageId())
				.target(target)
				.code(code)
				.timestamp(System.currentTimeMillis())
				.build();
	}

	public static String generateMessageId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Getter
	@AllArgsConstructor
	public enum VerificationType {
		EMAIL("EMAIL"),
		SMS("SMS");

		private String value;
	}
}
