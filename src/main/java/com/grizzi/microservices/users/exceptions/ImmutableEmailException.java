package com.grizzi.microservices.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason = "Email address cannot be changed")
public class ImmutableEmailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3399105433678358823L;

}