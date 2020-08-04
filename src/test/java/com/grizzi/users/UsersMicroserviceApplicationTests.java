package com.grizzi.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.grizzi.microservices.users.UsersMicroserviceApplication;

@SpringBootTest(classes = UsersMicroserviceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class UsersMicroserviceApplicationTests {
	
	@Test
	void contextLoads() throws Exception {
	}
}
