package com.grizzi.microservices.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NullParamException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078637455269561871L;
	
	public NullParamException(String message) {
		super(message);
	}

}
