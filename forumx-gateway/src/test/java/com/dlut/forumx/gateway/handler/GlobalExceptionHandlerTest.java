package com.dlut.forumx.gateway.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	@InjectMocks
	private GlobalExceptionHandler handler;

	@Mock
	private ObjectMapper objectMapper;

	private ServerWebExchange exchange;
	private ServerHttpResponse response;
	private DataBufferFactory bufferFactory;

	@BeforeEach
	void setUp() {
		bufferFactory = new DefaultDataBufferFactory();
		exchange = MockServerWebExchange.from(
				MockServerHttpRequest.get("/api/v1/users/1").build());
		response = exchange.getResponse();
	}

	@Test
	void shouldHandleResponseStatusException() throws JsonProcessingException {
		ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

		when(objectMapper.writeValueAsBytes(any(ResultDTO.class))).thenReturn(new byte[0]);

		handler.handle(exchange, ex).block();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	void shouldHandleGenericException() throws JsonProcessingException {
		RuntimeException ex = new RuntimeException("Internal server error");

		when(objectMapper.writeValueAsBytes(any(ResultDTO.class))).thenReturn(new byte[0]);

		handler.handle(exchange, ex).block();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	void shouldReturnErrorWhenResponseIsCommitted() {
		response.setStatusCode(HttpStatus.OK);
		response.writeWith(Mono.just(bufferFactory.wrap("committed".getBytes()))).block();

		RuntimeException ex = new RuntimeException("test");

		handler.handle(exchange, ex)
				.doOnError(e -> assertThat(e).isInstanceOf(RuntimeException.class))
				.onErrorResume(e -> Mono.empty())
				.block();
	}

	@Test
	void shouldHandleJsonProcessingException() throws JsonProcessingException {
		RuntimeException ex = new RuntimeException("test");

		JsonProcessingException mockJsonEx = mock(JsonProcessingException.class);
		when(mockJsonEx.getMessage()).thenReturn("JSON error");

		doThrow(mockJsonEx)
				.when(objectMapper).writeValueAsBytes(any(ResultDTO.class));

		handler.handle(exchange, ex).block();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void shouldIncludePathInResponse() throws JsonProcessingException {
		RuntimeException ex = new RuntimeException("test");

		when(objectMapper.writeValueAsBytes(any(ResultDTO.class))).thenReturn(new byte[0]);

		handler.handle(exchange, ex).block();

		verify(objectMapper).writeValueAsBytes(argThat((ResultDTO<?> dto) -> dto.getPath().equals("/api/v1/users/1")));
	}

	@Test
	void shouldUseExceptionMessageInResponse() throws JsonProcessingException {
		String errorMessage = "Custom error message";
		RuntimeException ex = new RuntimeException(errorMessage);

		when(objectMapper.writeValueAsBytes(any(ResultDTO.class))).thenReturn(new byte[0]);

		handler.handle(exchange, ex).block();

		verify(objectMapper).writeValueAsBytes(argThat((ResultDTO<?> dto) -> dto.getMessage().equals(errorMessage)));
	}
}
