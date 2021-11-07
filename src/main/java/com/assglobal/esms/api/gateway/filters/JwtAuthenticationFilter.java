package com.assglobal.esms.api.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.assglobal.esms.api.gateway.config.RouterValidator;
import com.assglobal.esms.api.gateway.model.LoginUser;
import com.assglobal.esms.api.gateway.services.UserService;
import com.assglobal.esms.api.gateway.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@RefreshScope
@Component
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {

	@Autowired
    private RouterValidator routerValidator;
    @Autowired
    private JwtTokenUtil jwtUtil;
    
    @Autowired
    private UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String jwtToken = null;
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
            {
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }
            
            final String requestTokenHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
            log.info("requestTokenHeader: "+requestTokenHeader);
            if (requestTokenHeader == null ) {
           	 log.warn("Authorization not found in headers");
           	 return this.onError(exchange, "Authorization header not available", HttpStatus.UNAUTHORIZED);
           }        
           
            if (requestTokenHeader != null && !requestTokenHeader.startsWith("Bearer ")) {
            	 log.warn("Token does not begin with Bearer String");
            	 return this.onError(exchange, "Token does not begin with Bearer String", HttpStatus.UNAUTHORIZED);
            }
            
            jwtToken = requestTokenHeader.split(" ")[1].trim();
                      
            String userId=jwtUtil.getUsernameFromToken(jwtToken);
            log.info("user name: "+userId);
            LoginUser userDetails = null;
			try {
				userDetails = userService.findUserByUsername(userId);
			} catch (Exception e) {
				log.error("Error occured while getting user details for jwt user: "+e.getLocalizedMessage());
				e.printStackTrace();
			}

            if(!jwtUtil.validateToken(jwtToken, userDetails))
            {
            	log.info("Invalid jwt token");
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            this.populateRequestWithHeaders(exchange, jwtToken);
            
        }
       
        return chain.filter(exchange);
    }


    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
    

}