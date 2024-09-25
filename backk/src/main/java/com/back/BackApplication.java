package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {
	    "com.back.mapper", 
	    "com.back.service", 
	    "com.back.config.security", 
	    "com.back.config.jwt",
	    "com.back.config", 
	    "com.back.mapper.UserUpdateMapper"
	})
	@SpringBootApplication
	public class BackApplication {
	    public static void main(String[] args) {
	        SpringApplication.run(BackApplication.class, args);
	    }
	}
