package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

public class ImmutableEmailError extends BadRequestError {

	protected void setMessage(String message) {
		super.setMessage("Email address cannot be changed");
	}

	@Override
	@ApiModelProperty(example = "Email address cannot be changed", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
