package com.assglobal.esms.api.gateway.services;

import com.assglobal.esms.api.gateway.model.ApiUsers;

public interface ApiUsersService {

	public ApiUsers getApiUser(String clientId, String productId);

	public ApiUsers saveApiUser(ApiUsers apiUser);
}
