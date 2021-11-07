package com.cloudapps.developer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
	
	@RequestMapping("/esmsFallback")
	public String esmsFallback() {
		return "ESMS API is down, it is taking too long time  to respond or it's down. Please try again later";
	}
	
}
