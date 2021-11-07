package com.assglobal.esms.api.gateway.model;

import java.io.Serializable;

public class AuthToken implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String jwttoken;
	
	public AuthToken(String jwttoken) {
		super();
		this.jwttoken = jwttoken;
	}

	public String getJwttoken() {
		return jwttoken;
	}
	
	
}
