package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

public class EmailAlreadyTakenError extends BadRequestError {

	@Override
	protected void setMessage(String message) {
		super.setMessage("Email address already taken");
	}

	@Override
	@ApiModelProperty(example = "Email address already taken", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}
}
