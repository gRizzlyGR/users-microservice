package com.grizzi.microservices.users.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grizzi.microservices.users.exceptions.EmailAlreadyTakenException;
import com.grizzi.microservices.users.exceptions.ImmutableEmailException;
import com.grizzi.microservices.users.exceptions.ImmutableIdException;
import com.grizzi.microservices.users.exceptions.NicknameAlreadyTakenException;
import com.grizzi.microservices.users.exceptions.QueryParamPasswordException;
import com.grizzi.microservices.users.exceptions.UserNotFoundException;
import com.grizzi.microservices.users.models.User;
import com.grizzi.microservices.users.repositories.UserRepository;
import com.grizzi.microservices.users.utils.UserSetter;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository dao;
	
	/**
	 * Return a list of users that match the provided criteria
	 * 
	 * @param paramToVal
	 * @return the filtered users, all of them, otherwise
	 */
	public List<User> findByParams(Map<String, Object> paramToVal) {
		// Create a specimen to use as model
		User specimen = UserSetter.fillUser(paramToVal);

		// Find all users that match this example, ignoring null values and case
		Example<User> example = Example.of(specimen, ExampleMatcher.matching().withIgnoreCase());

		// If the example has not set fields, return all users
		return dao.findAll(example);
	}

	/**
	 * Check if the email is already taken
	 * 
	 * @param email
	 * @return
	 */
	private boolean alreadyTakenEmail(String email) {
		List<User> users = dao.findByEmail(email);

		return checkIfValueBelongsToUser(email, users);
	}

	/**
	 * Check if the nickname is already taken
	 * 
	 * @param nickname
	 * @return
	 */
	private boolean alreadyTakenNickname(String nickname) {
		List<User> users = dao.findByNickname(nickname);

		return checkIfValueBelongsToUser(nickname, users);
	}

	/**
	 * Check if a certain value belongs to one of the users of the list
	 * 
	 * @param id
	 * @param value
	 * @param users
	 * @return
	 */
	private boolean checkIfValueBelongsToUser(String value, List<User> users) {
		return value != null && !users.isEmpty();
	}

	/**
	 * Validate user properties before creation
	 * 
	 * @param user
	 */
	public void validateForCreation(User user) {
		// Id cannot be changed
		if (user.getId() != null) {
			log.debug("Cannot change ID - 400 Bad Request");
			throw new ImmutableIdException();
		}

		if (alreadyTakenEmail(user.getEmail())) {
			log.debug("Email already taken: {} - 400 Bad Request", user.getEmail());
			throw new EmailAlreadyTakenException();
		}

		if (alreadyTakenNickname(user.getNickname())) {
			log.debug("Nickname already taken: {} - 400 Bad Request", user.getNickname());
			throw new NicknameAlreadyTakenException();
		}
	}

	/**
	 * Validate user properties before updating
	 * 
	 * @param id
	 * @param updates
	 */
	public void validateForUpdate(int id, User user) {
		if (!dao.existsById(id)) {
			throw new UserNotFoundException();
		}

		// Id cannot be changed
		if (user.getId() != null) {
			log.debug("Cannot change ID - 400 Bad Request");
			throw new ImmutableIdException();
		}

		if (user.getEmail() != null) {
			log.debug("Cannot change email - 400 Bad Request");
			throw new ImmutableEmailException();
		}

		if (alreadyTakenNickname(user.getNickname())) {
			throw new NicknameAlreadyTakenException();
		}
	}

	/**
	 * Update user with new info
	 * 
	 * @param id
	 * @param updates
	 * @return
	 */
	public void update(int id, User userToReadFrom) {

		// Validate user
		validateForUpdate(id, userToReadFrom);

		// Find user of which replace data
		User userToUpdate = dao.findById(id).get();

		// Replace data and save
		User updatedUser = UserSetter.fillUser(userToUpdate, userToReadFrom);

		// Encode password
		String password = updatedUser.getPassword();
		updatedUser.setPassword(passwordEncoder().encode(password));
		dao.save(updatedUser);
	}

	/**
	 * Validate request checking the quey params
	 * 
	 * @param params
	 */
	public void validateByQueryParams(Set<String> params) {
		// Password cannot be used for filtering. Throw 403
		if (passwordAsQueryParam(params)) {
			log.debug("Password used as query parameter: %s - 403 Forbidden");
			throw new QueryParamPasswordException();
		}
	}

	/**
	 * Check if some query params do not match the user POJO's fields or the client
	 * is trying to filter via the password
	 * 
	 * @param queryParams
	 * @return
	 */
	boolean areParamsValid(Set<String> queryParams) {
		return UserSetter.getUserFields().containsAll(queryParams);
	}

	/**
	 * Check if the password is among the query params
	 * 
	 * @param queryParams
	 * @return
	 */
	private boolean passwordAsQueryParam(Set<String> queryParams) {
		return queryParams.contains("password");
	}

	/**
	 * Register new user, validating it and encoding the password
	 * 
	 * @param user
	 * @return
	 */
	public User register(User user) {
		validateForCreation(user);

		User toSave = new User();
		toSave.setFirstName(user.getFirstName());
		toSave.setLastName(user.getLastName());
		toSave.setNickname(user.getNickname());
		toSave.setCountry(user.getCountry());
		toSave.setEmail(user.getEmail());
		toSave.setPassword(passwordEncoder().encode(user.getPassword()));

		return dao.save(toSave);
	}

	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	/**
	 * Remove user by id
	 * 
	 * @param id
	 */
	public void remove(int id) {
		if (!dao.existsById(id))
			throw new UserNotFoundException();

		dao.deleteById(id);
	}
}
