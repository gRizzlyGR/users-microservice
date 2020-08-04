package com.grizzi.microservices.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Email address already taken")
public class EmailAlreadyTakenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1551020555380181227L;

}
