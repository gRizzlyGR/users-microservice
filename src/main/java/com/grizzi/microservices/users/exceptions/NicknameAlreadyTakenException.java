package com.grizzi.microservices.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Nickname already taken")
public class NicknameAlreadyTakenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3868312805665216027L;

}
