package com.grizzi.microservices.users.controllers;

import static com.grizzi.microservices.users.utils.JsonSerializer.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
class UpdateUserControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	JmsTemplate jmsTemplate;

	@Test
	void itShouldReturn404NotFound() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(patch("/users/1")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void itShouldReturn400ForSettingTheId() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String idPatch = String.format("{\"id\":%d}", 1);

		mvc.perform(patch("/users/1")
				.content(idPatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Id cannot be set")));
	}

	@Test
	void itShouldReturn400ForSettingAnAlreadyTakenEmail() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String emailPatch = "{\"email\":\"new@email.com\"}";

		mvc.perform(patch("/users/1")
				.content(emailPatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Email address cannot be changed")));

	}

	@Test
	void itShouldUpdateTheNickname() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String newNickname = "newNickname";
		String nicknamePatch = String.format("{\"nickname\":\"%s\"}", newNickname);

		mvc.perform(patch("/users/1")
				.content(nicknamePatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		mvc.perform(get("/users/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.nickname").value(newNickname));
	}

	@Test
	void itShouldReturn400ForNicknameAlreadyTaken() throws Exception {
		// Conflicting nickname reserved by first user
		String reservedNickname = "reservedNickname";
		User mockUser1 = TestUtils.generateUser();

		mockUser1.setNickname(reservedNickname);

		mvc.perform(post("/users")
				.content(asJsonString(mockUser1))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		// Create second user with different email and nickname from the first one
		User mockUser2 = TestUtils.generateUser();

		mockUser2.setEmail("unique@email.com");
		mockUser2.setNickname("uniqueNickname");

		mvc.perform(post("/users")
				.content(asJsonString(mockUser2))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		// Try to update the second user

		String nicknamePatch = String.format("{\"nickname\":\"%s\"}", reservedNickname);

		mvc.perform(patch("/users/2")
				.content(nicknamePatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Nickname already taken")));
	}

	@Test
	void itShouldUpdateTheFirstName() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String newFirstName = "newFirstName";
		String firstNamePatch = String.format("{\"firstName\":\"%s\"}", newFirstName);

		mvc.perform(patch("/users/1")
				.content(firstNamePatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		mvc.perform(get("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value(newFirstName));
	}

	@Test
	void itShouldUpdateTheLastName() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String newLastName = "newLastName";
		String lastNamePatch = String.format("{\"lastName\":\"%s\"}", newLastName);

		mvc.perform(patch("/users/1")
				.content(lastNamePatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		mvc.perform(get("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName").value(newLastName));
	}

	@Test
	void itShouldUpdateTheCountry() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String newCountry = "newCountry";
		String countryPatch = String.format("{\"country\":\"%s\"}", newCountry);

		mvc.perform(patch("/users/1")
				.content(countryPatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		mvc.perform(get("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.country").value(newCountry));

	}

	@Test
	void itShouldUpdateThePassword() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String newPassword = "newCountry";
		String passwordPatch = String.format("{\"password\":\"%s\"}", newPassword);

		mvc.perform(patch("/users/1")
				.content(passwordPatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		mvc.perform(get("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.password").doesNotExist());

	}

	@Test
	void itShouldUpdateABunchOfFields() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String newFirstName = "newFirstName";
		String newCountry = "newCountry";
		String somePatch = String.format("{\"country\":\"%s\",\"firstName\":\"%s\"}", newCountry, newFirstName);

		mvc.perform(patch("/users/1")
				.content(somePatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		mvc.perform(get("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.country").value(newCountry))
				.andExpect(jsonPath("$.firstName").value(newFirstName));
	}

	@Test
	void isShouldReturn400ForNullValue() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String nullCountryPatch = "{\"country\":null}";

		String errorMessage = mvc.perform(patch("/users/1")
				.content(nullCountryPatch)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException()
				.getMessage();

		assertTrue(errorMessage.contains("JSON parse error: null"));
	}

}
