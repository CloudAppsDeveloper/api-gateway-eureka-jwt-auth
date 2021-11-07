package com.assglobal.esms.api.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter implements GatewayFilter {

	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		log.info("Inside GlobalFilter.apply() method");

		ServerHttpRequest request = exchange.getRequest();

		// validating Authorisation header in the request

		if (!request.getHeaders().containsKey("Authorization")) {
			log.info("No Authorization header");
			return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
		}
		
		return chain.filter(exchange.mutate().request(request).build());

	}

	

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		log.info("GlobalFilter onError....!");
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

}
