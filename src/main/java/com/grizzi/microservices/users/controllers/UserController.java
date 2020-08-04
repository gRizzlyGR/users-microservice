package com.grizzi.microservices.users.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.grizzi.microservices.users.exceptions.UserNotFoundException;
import com.grizzi.microservices.users.models.NoIdUser;
import com.grizzi.microservices.users.models.User;
import com.grizzi.microservices.users.models.errors.badrequest.UmbrellaBadRequestError;
import com.grizzi.microservices.users.models.errors.badrequest.ValueNotNumericError;
import com.grizzi.microservices.users.models.errors.forbidden.QueryParamPasswordError;
import com.grizzi.microservices.users.models.errors.notfound.UserNotFoundError;
import com.grizzi.microservices.users.repositories.UserRepository;
import com.grizzi.microservices.users.services.QueueService;
import com.grizzi.microservices.users.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

/**
 * Base class for user controllers
 * 
 * @author giuseppe
 *
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Users management system", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	protected UserRepository dao;

	@Autowired
	protected UserService userService;

	@Autowired
	protected QueueService queueService;

	/**
	 * Add user to the DB
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/users")
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation(value = "Add a user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "User added successully.", responseHeaders = @ResponseHeader(name = "location", response = URI.class)),
			@ApiResponse(code = 400, message = "Email address already taken"
					+ ", Email address cannot be changed"
					+ ", JSON parse error: null"
					+ ", JSON parse error: Unrecognized field"
					+ ", Id cannot be set"
					+ ", Nickname already taken", response = UmbrellaBadRequestError.class)
	})
	public ResponseEntity<String> addUser(@Valid @RequestBody NoIdUser user) {

		// Return errors in case the validation fails
		User saved = userService.register(user);
		URI location = URI.create("users/" + saved.getId());

		String userCreatedMessage = String.format("User created with with location: %s", location);

		log.info(userCreatedMessage);
		queueService.send("user-created-queue", userCreatedMessage);

		return ResponseEntity
				.created(location)
				.build();
	}

	/**
	 * Get users filtered by query params. If none, return all users
	 * 
	 * @param paramToVal - mapping of query params and their values
	 * @return
	 */
	@GetMapping(path = "/users")
	@ResponseStatus(code = HttpStatus.OK)
	@ApiOperation(value = "Retrieve a list of user, potentially filtered using query params")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User(s) retrieved successfully"),
			@ApiResponse(code = 403, message = "Password field used as query parameter", response = QueryParamPasswordError.class)
	})
	public List<User> getUsers(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "nickname", required = false) String nickname,
			@ApiParam(hidden = true) @RequestParam Map<String, Object> paramToVal) {

		// Validate before processing request
		userService.validateByQueryParams(paramToVal.keySet());

		return userService.findByParams(paramToVal);
	}

	/**
	 * Get user by id as path parameter
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/users/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@ApiOperation(value = "Retrieve a single user by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User retrieved successfully"),
			@ApiResponse(code = 400, message = "Value is not numeric", response = ValueNotNumericError.class),
			@ApiResponse(code = 404, message = "User not found", response = UserNotFoundError.class),
	})
	public User getUserByPathVariableId(@PathVariable("id") int id) {
		return dao.findById(id).orElseThrow(() -> new UserNotFoundException());
	}

	/**
	 * Update user using the map updates
	 * 
	 * @param id
	 * @param updates
	 * @return
	 */
	@PatchMapping("/users/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Update a single user by id")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "User updated successfully"),
			@ApiResponse(code = 400, message = "Email address already taken"
					+ ", Email address cannot be changed"
					+ ", JSON parse error: null"
					+ ", Id cannot be set"
					+ ", Nickname already taken", response = UmbrellaBadRequestError.class),
			@ApiResponse(code = 404, message = "User not found", response = UserNotFoundError.class),
	})
	public ResponseEntity<String> updateUser(@PathVariable("id") int id, @RequestBody User user) {
		// Validate user and throw errors in case
		userService.update(id, user);

		String userUpdatedMessage = String.format("User %d updated", id);

		log.info(userUpdatedMessage);
		queueService.send("user-updated-queue", userUpdatedMessage);

		return ResponseEntity
				.noContent()
				.build();
	}

	/**
	 * Delete user by id
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("users/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete a user by id")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "User deleted successfully"),
			@ApiResponse(code = 400, message = "Value is not numeric", response = ValueNotNumericError.class),
			@ApiResponse(code = 404, message = "User not found", response = UserNotFoundError.class),
	})
	public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
		userService.remove(id);

		String userDeletedMessage = String.format("User %d has been deleted", id);
		log.info(userDeletedMessage);
		queueService.send("user-deleted-queue", userDeletedMessage);

		return ResponseEntity
				.noContent()
				.build();
	}
}
