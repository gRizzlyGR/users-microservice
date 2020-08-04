package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

public class ValueNotNumericError extends BadRequestError {

	protected void setMessage(String message) {
		super.setMessage("Value is not numeric");
	}

	@Override
	@ApiModelProperty(example = "Value is not numeric", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
