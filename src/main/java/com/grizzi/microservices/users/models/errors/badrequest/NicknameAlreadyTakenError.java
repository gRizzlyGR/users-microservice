package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

public class NicknameAlreadyTakenError extends BadRequestError {

	protected void setMessage(String message) {
		super.setMessage("Nickname already taken");
	}

	@Override
	@ApiModelProperty(example = "Nickname already taken", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
