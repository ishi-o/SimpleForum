package com.dlut.forumx.gateway.handler;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-1)
@Configuration
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

	private final ObjectMapper objectMapper;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		ServerHttpResponse response = exchange.getResponse();

		if (response.isCommitted()) {
			return Mono.error(ex);
		}

		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		if (ex instanceof ResponseStatusException) {
			response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
		} else {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String path = exchange.getRequest().getURI().getPath();
		ResultDTO<Void> result = ResultDTO.error(
				response.getStatusCode().value(),
				ex.getMessage(),
				path);

		return response.writeWith(Mono.fromSupplier(() -> {
			DataBufferFactory bufferFactory = response.bufferFactory();
			try {
				byte[] bytes = objectMapper.writeValueAsBytes(result);
				return bufferFactory.wrap(bytes);
			} catch (JsonProcessingException e) {
				log.error("Error writing response", e);
				return bufferFactory.wrap(new byte[0]);
			}
		}));
	}
}
