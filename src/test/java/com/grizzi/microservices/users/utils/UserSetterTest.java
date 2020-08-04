package com.grizzi.microservices.users.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.grizzi.microservices.users.exceptions.NullParamException;
import com.grizzi.microservices.users.models.User;

@SpringBootTest
class UserSetterTest {

	@Test
	final void testFillUser() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", 1);
		map.put("firstName", "testName");

		User user = UserSetter.fillUser(map);

		assertEquals("testName", user.getFirstName());
		assertEquals(1, user.getId());

		assertNull(UserSetter.fillUser(null).getId());
		assertNull(UserSetter.fillUser(Collections.emptyMap()).getId());
	}

	@Test
	final void testGetSetter() throws Exception {	
		assertTrue(UserSetter.getSetter("id", 1).isPresent());

		assertTrue(UserSetter.getSetter("id", 1).isPresent());
		assertTrue(UserSetter.getSetter("lastName", "").isPresent());
		assertTrue(UserSetter.getSetter("firstName", "").isPresent());
		assertTrue(UserSetter.getSetter("nickname", "").isPresent());
		assertTrue(UserSetter.getSetter("country", "").isPresent());
		assertTrue(UserSetter.getSetter("email", "").isPresent());
		assertTrue(UserSetter.getSetter("password", "").isPresent());

		assertThrows(NoSuchMethodException.class, () -> UserSetter.getSetter("not present", ""));
		assertEquals(Optional.empty(), UserSetter.getSetter("id", null));
		assertEquals(Optional.empty(), UserSetter.getSetter(null, null));
	}

	@Test
	final void testInvokeSetter() throws Exception {
		User user = new User();

		UserSetter.invokeSetter(user, "firstName", "testName");

		assertEquals("testName", user.getFirstName());

		assertThrows(NoSuchMethodException.class, () -> UserSetter.invokeSetter(user, "not present", ""));
		assertThrows(IllegalArgumentException.class, () -> UserSetter.invokeSetter(null, "firstName", ""));
	}

}
