package com.dlut.forumx.gateway.controller;

import com.dlut.forumx.commons.dto.ResultDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

	@RequestMapping("/fallback/user")
	public Mono<ResultDTO<Void>> userFallback() {
		return Mono.just(ResultDTO.error(503, "用户服务暂时不可用，请稍后重试"));
	}

	@RequestMapping("/fallback/content")
	public Mono<ResultDTO<Void>> contentFallback() {
		return Mono.just(ResultDTO.error(503, "内容服务暂时不可用，请稍后重试"));
	}

	@RequestMapping("/fallback/interaction")
	public Mono<ResultDTO<Void>> interactionFallback() {
		return Mono.just(ResultDTO.error(503, "互动服务暂时不可用，请稍后重试"));
	}

	@RequestMapping("/fallback/default")
	public Mono<ResultDTO<Void>> defaultFallback() {
		return Mono.just(ResultDTO.error(503, "服务暂时不可用，请稍后重试"));
	}
}
