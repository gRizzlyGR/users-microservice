package com.grizzi.microservices.users.controllers;

import static com.grizzi.microservices.users.utils.JsonSerializer.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.grizzi.microservices.users.models.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class DeleteUserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	JmsTemplate jmsTemplate;

	@Test
	void itShouldReturn404() throws Exception {
		mvc.perform(delete("/users/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void isShouldRemoveTheUserById() throws  Exception{
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		
		mvc.perform(delete("/users/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	void itShouldReturn400ForNonNumericId() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		
		mvc.perform(delete("/users/invalid")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Value is not numeric")));
	}

}
