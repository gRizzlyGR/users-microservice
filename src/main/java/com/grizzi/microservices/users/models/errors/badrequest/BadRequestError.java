package com.grizzi.microservices.users.models.errors.badrequest;

import org.springframework.http.HttpStatus;

import com.grizzi.microservices.users.models.errors.Error;

import io.swagger.annotations.ApiModelProperty;

/**
 * Bad request base model
 * @author giuseppe
 *
 */
public abstract class BadRequestError extends Error {

	@Override
	protected void setStatus(Integer status) {
		super.setStatus(HttpStatus.BAD_REQUEST.value());
	}

	@Override
	protected void setError(String error) {
		super.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
	}

	@Override
	@ApiModelProperty(example = "400", value = "Status")
	public Integer getStatus() {
		return super.getStatus();
	}

	@Override
	@ApiModelProperty(example = "Bad Request", value = "Error")
	public String getError() {
		return super.getError();
	}
}
