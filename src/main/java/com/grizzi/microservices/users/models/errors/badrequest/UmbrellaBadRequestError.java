package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Since swagger cannot handle more than one model for describing error
 * responses, this class reunites all errors that could occur while using an API
 * 
 * @author giuseppe
 *
 */
public class UmbrellaBadRequestError extends BadRequestError {
	@Override
	protected void setMessage(String message) {
		super.setMessage("Email address already taken");
	}

	@Override
	@ApiModelProperty(value = "Message", allowableValues = "Email address already taken"
			+ ", Email address cannot be changed"
			+ ", JSON parse error: null"
			+ ", JSON parse error: Unrecognized field"
			+ ", Id cannot be set"
			+ ", Nickname already taken")
	public String getMessage() {
		return super.getMessage();
	}
}
