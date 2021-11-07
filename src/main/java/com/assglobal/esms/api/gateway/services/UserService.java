package com.assglobal.esms.api.gateway.services;

import com.assglobal.esms.api.gateway.model.LoginUser;

public interface UserService {

	public LoginUser save(LoginUser user);
	public LoginUser findUserByUsername(String userId) throws Exception;

}
