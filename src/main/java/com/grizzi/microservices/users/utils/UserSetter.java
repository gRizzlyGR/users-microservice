package com.grizzi.microservices.users.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grizzi.microservices.users.exceptions.NullParamException;
import com.grizzi.microservices.users.models.User;

/**
 * Handle user specimens generation, based on custom data
 * 
 * @author giuseppe
 *
 */
@Component
public class UserSetter {
	private static final Logger log = LoggerFactory.getLogger(UserSetter.class);

	private static Map<String, String> paramToSetter = initMap();

	/**
	 * Create a user specimen to use for query the database, matching the data
	 * provided by clients
	 * 
	 * @param paramToVal
	 * @return user specimen
	 */
	public static User fillUser(Map<String, Object> paramToVal) {
		User newUser = new User();

		return fillUser(newUser, paramToVal);
	}

	/**
	 * Fill the input user with the provided values. Usually used to replace data of
	 * an existing user
	 * 
	 * @param user
	 * @param paramToVal
	 * @return
	 */
	public static User fillUser(User user, Map<String, Object> paramToVal) {
		if (paramToVal != null) {
			paramToVal.forEach((paramName, paramValue) -> {
				try {
					invokeSetter(user, paramName, paramValue);
				} catch (NoSuchMethodException | SecurityException e) {
					log.warn("Unable to invoke setter for " + paramName + ": " + e.getMessage());
				}
			});
		}

		return user;
	}

	public static User fillUser(User userToFill, User userToReadFrom) {
		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> map = mapper
				.convertValue(userToReadFrom, new TypeReference<Map<String, Object>>() {});

		return fillUser(userToFill, map);
	}

	/**
	 * Return the user POJO's fields
	 * 
	 * @return
	 */
	public static Set<String> getUserFields() {
		return paramToSetter.keySet();
	}

	/**
	 * Init a mapping among query parameters names and the User POJO's setters
	 * 
	 * @return
	 */
	private static Map<String, String> initMap() {
		Map<String, String> paramToSetter = new ConcurrentHashMap<String, String>();

		paramToSetter.put("id", "setId");
		paramToSetter.put("firstName", "setFirstName");
		paramToSetter.put("lastName", "setLastName");
		paramToSetter.put("nickname", "setNickname");
		paramToSetter.put("country", "setCountry");
		paramToSetter.put("email", "setEmail");
		paramToSetter.put("password", "setPassword");

		return paramToSetter;
	}

	/**
	 * Return the setter method to invoke based on the query parameter name and its
	 * value
	 * 
	 * @param paramName
	 * @param paramValue
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	static Optional<Method> getSetter(String paramName, Object paramValue) throws NoSuchMethodException,
			SecurityException {
		if (paramName == null || paramValue == null) {
			return Optional.empty();
		}

		String setterName = paramToSetter.get(paramName);

		if (setterName == null) {
			throw new NoSuchMethodException("No setter for " + paramName);
		}

		Method setter = User.class.getMethod(setterName, paramValue.getClass());
		return Optional.of(setter);
	}

	/**
	 * Effectively invoke the method using reflection
	 * 
	 * @param user
	 * @param paramName
	 * @param paramValue
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	static void invokeSetter(User user, String paramName, Object paramValue) throws NoSuchMethodException,
			SecurityException {
		if (user == null) {
			throw new IllegalArgumentException("User is null");
		}

		Optional<Method> optMethod = getSetter(paramName, paramValue);

		// If present, set the value for the user specimen, otherwise ignore it
		optMethod.ifPresent(setter -> {
			try {
				setter.invoke(user, paramValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.warn("Unable to set value for " + paramName);
			}
		});
	}
}
