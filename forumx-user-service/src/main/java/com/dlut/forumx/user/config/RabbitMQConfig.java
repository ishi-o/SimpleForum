package com.dlut.forumx.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Bean
	public Exchange verificationExchange() {
		return new ExchangeBuilder.ConsistentHashExchangeBuilder("exchange.verification").build();
	}

	@Bean
	public Queue smsQueue() {
		return new Queue("queue.verification.sms");
	}

	@Bean
	public Queue emailQueue() {
		return new Queue("queue.verification.email");
	}

	@Bean
	public Binding smsBinding() {
		return new Binding("queue.verification.sms", DestinationType.QUEUE, "exchange.verification.direct",
				"binding.verification.sms", null);
	}

	@Bean
	public Binding emailBinding() {
		return new Binding("queue.verification.email", DestinationType.QUEUE, "exchange.verification.direct",
				"binding.verification.email", null);
	}
}
