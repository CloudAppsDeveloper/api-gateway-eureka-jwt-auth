package com.cloudapps.developer.services;

import com.cloudapps.developer.model.LoginUser;

public interface UserService {

	public LoginUser save(LoginUser user);
	public LoginUser findUserByUsername(String userId) throws Exception;

}
