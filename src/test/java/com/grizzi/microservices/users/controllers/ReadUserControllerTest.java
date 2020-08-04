package com.grizzi.microservices.users.controllers;

import static com.grizzi.microservices.users.utils.JsonSerializer.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.util.Lists;
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
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReadUserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	JmsTemplate jmsTemplate;

	@Test
	void itShouldReturnAnEmptyList() throws Exception {
		mvc.perform(get("/users")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void itShouldReturnAUserAndHidePassword() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void itShouldReturn404NotFound() throws Exception {
		mvc.perform(get("/users/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void itShouldReturnAUserByIDasQueryParam() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?id=1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[0].id").value("1"));
	}

	@Test
	void itShouldReturnUsersByFirstName() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?firstName=test first name")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[0].firstName").exists())
				.andExpect(jsonPath("$[0].firstName").value("test first name"));
	}

	@Test
	void itShouldReturnUsersByLastName() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?lastName=test last name")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[0].lastName").exists())
				.andExpect(jsonPath("$[0].lastName").value("test last name"));
	}

	@Test
	void itShouldReturnUsersByNickname() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?nickname=testNickName")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[0].nickname").exists())
				.andExpect(jsonPath("$[0].nickname").value("testNickName"));
	}

	@Test
	void itShouldReturnUsersByCountry() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?country=test country")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[0].country").exists())
				.andExpect(jsonPath("$[0].country").value("test country"));
	}

	@Test
	void itShouldReturnUsersByEmail() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?email=test@email.com")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[0].email").exists())
				.andExpect(jsonPath("$[0].email").value("test@email.com"));
	}

	@Test
	void itShouldThrow403ForbiddenForUsingPasswordAsQueryParam() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		mvc.perform(get("/users?password=test password")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andExpect(status().reason(containsString("Password field cannot be used as query parameter")));
	}

	@Test
	void itShouldGroupUsersWithCommonDataUsingQueryParams() throws Exception {

		// Generate three users on which we set some common data, two by two
		User mockUser1 = TestUtils.generateUser();
		User mockUser2 = TestUtils.generateUser();
		User mockUser3 = TestUtils.generateUser();

		String sameFirstName = "same first name";
		String sameLastName = "same last name";
		String sameCountry = "same country";

		// Same first name: 1 & 2
		mockUser1.setFirstName(sameFirstName);
		mockUser2.setFirstName(sameFirstName);

		// Same last name: 1 & 3
		mockUser1.setLastName(sameLastName);
		mockUser3.setLastName(sameLastName);

		// Same country: 2 & 3
		mockUser2.setCountry(sameCountry);
		mockUser3.setCountry(sameCountry);
		
		// Need to change email and nickname to avoid bad requests
		mockUser1.setEmail("mockUser1@email.com");
		mockUser2.setEmail("mockUser2@email.com");
		mockUser3.setEmail("mockUser3@email.com");
		
		mockUser1.setNickname("mockUser1");
		mockUser2.setNickname("mockUser2");
		mockUser3.setNickname("mockUser3");

		// Post all objects
		Lists.list(mockUser1, mockUser2, mockUser3).forEach(mockUser -> {
			try {
				mvc.perform(post("/users")
						.content(asJsonString(mockUser))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// Query first name
		mvc.perform(get("/users?firstName=" + sameFirstName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstName").exists())
				.andExpect(jsonPath("$[0].firstName").value(sameFirstName));

		// Query last name
		mvc.perform(get("/users?lastName=" + sameLastName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].lastName").exists())
				.andExpect(jsonPath("$[0].lastName").value(sameLastName));

		// Query country
		mvc.perform(get("/users?country=" + sameCountry)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].country").exists())
				.andExpect(jsonPath("$[0].country").value(sameCountry));

		// No query
		mvc.perform(get("/users")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(3)));
	}
	
	@Test
	void itShouldReturn400ForNonNumericId() throws Exception {
		User mockUser = TestUtils.generateUser();

		mvc.perform(post("/users")
				.content(asJsonString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		
		mvc.perform(get("/users/invalid")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(containsString("Value is not numeric")));
	}
}
