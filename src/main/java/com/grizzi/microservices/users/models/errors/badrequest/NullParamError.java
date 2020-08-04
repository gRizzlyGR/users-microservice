package com.grizzi.microservices.users.models.errors.badrequest;

import io.swagger.annotations.ApiModelProperty;

public class NullParamError extends BadRequestError {

	protected void setMessage(String message) {
		super.setMessage("JSON parse error: null");
	}

	@Override
	@ApiModelProperty(example = "JSON parse error: null", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
