package com.grizzi.microservices.users.controllers;

import static com.grizzi.microservices.users.utils.JsonSerializer.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.grizzi.microservices.users.models.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CreateUserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	JmsTemplate jmsTemplate;

	@Test
	void itShouldReturn201CreatedAndSetLocation() throws Exception {
		MvcResult result = mvc.perform(post("/users")
				.content(asJsonString(TestUtils.generateUser()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		String location = result.getResponse().getHeader("Location");
		assertNotNull(location);
	}

	@Test
	void itShouldReturn400ForSetId() throws Exception {
		User mockUser = TestUtils.generateUser();
		mockUser.setId(1);
		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Id cannot be set")));
	}

	@Test
	void itShouldReturn400ForNicknameAlreadyTaken() throws Exception {
		User mockUser = TestUtils.generateUser();
		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		mockUser.setEmail("unique@email.com");

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Nickname already taken")));
	}

	@Test
	void itShouldReturn400ForEmailAlreadyTaken() throws Exception {
		User mockUser = TestUtils.generateUser();
		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		mockUser.setNickname("unique");

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Email address already taken")));
	}

	@Test
	void itShouldReturn400ForNull() throws Exception {
		mvc.perform(post("/users")
				.content((byte[]) null)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void itShouldReturn400ForNicknameWithBlanks() throws Exception {
		User mockUser = TestUtils.generateUser();
		mockUser.setNickname("not valid because there are blanks");
		String errorMessage = mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException()
				.getMessage();
		
		assertTrue(errorMessage.contains("Nickname cannot contain blanks"));
	}

	@Test
	void itShouldReturn400ForInvalidEmail() throws Exception {
		User mockUser = TestUtils.generateUser();
		mockUser.setEmail("invalid email");
		String errorMessage = mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException()
				.getMessage();

		assertTrue(errorMessage.contains("Invalid email"));
	}
}
