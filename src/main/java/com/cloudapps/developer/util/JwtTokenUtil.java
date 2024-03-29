package com.cloudapps.developer.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cloudapps.developer.model.LoginUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil{

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expirationTime;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(LoginUser userVO, String type) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", userVO.getId());
		claims.put("role", userVO.getRole());
		claims.put("email", userVO.getEmail());
		claims.put("username", userVO.getUsername());
		return doGenerateToken(claims, userVO.getUsername(), type);
	}

	private String doGenerateToken(Map<String, Object> claims, String username, String type) {
		long expirationTimeLong;
		if ("ACCESS".equals(type)) {
			expirationTimeLong = Long.parseLong(expirationTime) * 1000;
		} else {
			expirationTimeLong = Long.parseLong(expirationTime) * 1000 * 5;
		}
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(key).compact();
	}

	// validate token
	public Boolean validateToken(String token, LoginUser userDetails) {
		final String username = getUsernameFromToken(token);
		boolean result=false;
		if(username.equals(userDetails.getUsername())){
			log.info("username matched");
			log.info("isTokenExpired: "+isTokenExpired(token));
			if(!isTokenExpired(token)) {
				log.info("token valid.");
				result=true;
			}
			else {
				log.warn("token expired");
			}
		}
		return result;
	}

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

}