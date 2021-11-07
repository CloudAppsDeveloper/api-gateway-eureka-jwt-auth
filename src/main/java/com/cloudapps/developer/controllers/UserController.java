package com.cloudapps.developer.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudapps.developer.model.ApiUsers;
import com.cloudapps.developer.model.LoginUser;
import com.cloudapps.developer.services.ApiUsersService;
import com.cloudapps.developer.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
	
    @Autowired
    private UserService userService;
    
    @Autowired ApiUsersService apiUsersService;
    
    @PostMapping("/signup")
    public LoginUser saveUser(@RequestBody LoginUser user,HttpSession session){
    	log.info("UserController::saveUser");
      return userService.save(user);
    }
    @PostMapping("/client/register")
    public ApiUsers saveClient(@RequestBody ApiUsers apiuser,HttpSession session){
    	log.info("UserController::saveClient");
      return apiUsersService.saveApiUser(apiuser);
    }
}
