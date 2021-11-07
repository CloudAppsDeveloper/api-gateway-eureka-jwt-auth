package com.cloudapps.developer.services;

import com.cloudapps.developer.model.ApiUsers;

public interface ApiUsersService {

	public ApiUsers getApiUser(String clientId, String productId);

	public ApiUsers saveApiUser(ApiUsers apiUser);
}
