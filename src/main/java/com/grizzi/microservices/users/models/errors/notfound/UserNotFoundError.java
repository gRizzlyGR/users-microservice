package com.grizzi.microservices.users.models.errors.notfound;

import io.swagger.annotations.ApiModelProperty;

public class UserNotFoundError extends NotFoundError {
	protected void setMessage(String message) {
		super.setMessage("User not found");
	}

	@Override
	@ApiModelProperty(example = "User not found", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
