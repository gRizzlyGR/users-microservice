package com.grizzi.microservices.users.controllers;

import com.grizzi.microservices.users.models.User;

public class TestUtils {

	static User generateUser() {
		User user = new User();
		user.setFirstName("test first name");
		user.setLastName("test last name");
		user.setNickname("testNickName");
		user.setCountry("test country");
		user.setEmail("test@email.com");
		user.setPassword("test password");
	
		return user;
	}

}
