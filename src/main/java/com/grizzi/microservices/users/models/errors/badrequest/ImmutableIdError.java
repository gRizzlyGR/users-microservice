package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

public class ImmutableIdError extends BadRequestError {

	protected void setMessage(String message) {
		super.setMessage("Id cannot be set");
	}

	@Override
	@ApiModelProperty(example = "Id cannot be set", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
