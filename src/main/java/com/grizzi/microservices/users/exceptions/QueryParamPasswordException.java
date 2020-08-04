package com.grizzi.microservices.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Password field cannot be used as query parameter")
public class QueryParamPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9085668624964259616L;

}
