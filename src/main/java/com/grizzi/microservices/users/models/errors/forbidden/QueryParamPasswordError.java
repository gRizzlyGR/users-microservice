package com.grizzi.microservices.users.models.errors.forbidden;

import io.swagger.annotations.ApiModelProperty;

/**
 * Password used as query param erro model
 * @author giuseppe
 *
 */
public class QueryParamPasswordError extends ForbiddenError {

	@Override
	protected void setMessage(String message) {
		super.setMessage("Password field cannot be used as query parameter");
	}

	@Override
	@ApiModelProperty(example = "Password field cannot be used as query parameter", value = "Message")
	public String getMessage() {
		return super.getMessage();
	}

}
