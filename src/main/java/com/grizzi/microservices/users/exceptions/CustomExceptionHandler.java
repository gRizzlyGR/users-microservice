package com.grizzi.microservices.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Value is not numeric")
	public NumberFormatException handleNumberFormatException(NumberFormatException nfe) {
		return nfe;
	}
}
