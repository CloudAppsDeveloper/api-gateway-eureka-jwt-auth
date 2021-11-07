package com.assglobal.esms.api.gateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assglobal.esms.api.gateway.model.LoginUser;
import com.assglobal.esms.api.gateway.repo.UsersRepository;

import lombok.extern.slf4j.Slf4j;

@Service(value = "userService")
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersRepository userDao;

	@Override
	public LoginUser save(LoginUser user) {
		return userDao.save(user);
	}
	@Override
	public LoginUser findUserByUsername(String userId) throws Exception {
		LoginUser user = userDao.findByUsername(userId);
		log.info("user details in db for the user: "+userId+" are : "+user);
		if (user == null) {
			log.info("user details not found in db");
			throw new Exception("Invalid username or password.");
		}
		return user;
	}

}