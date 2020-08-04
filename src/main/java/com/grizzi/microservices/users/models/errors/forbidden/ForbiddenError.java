package com.grizzi.microservices.users.models.errors.forbidden;

import org.springframework.http.HttpStatus;

import com.grizzi.microservices.users.models.errors.Error;

import io.swagger.annotations.ApiModelProperty;

/**
 * Forbidden error base model
 * @author giuseppe
 *
 */
public abstract class ForbiddenError extends Error {

	@Override
	protected void setStatus(Integer status) {
		super.setStatus(HttpStatus.FORBIDDEN.value());
	}

	@Override
	protected void setError(String error) {
		super.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
	}

	@Override
	@ApiModelProperty(example = "403", value = "Status")
	public Integer getStatus() {
		return super.getStatus();
	}

	@Override
	@ApiModelProperty(example = "Forbidden", value = "Error")
	public String getError() {
		return super.getError();
	}
	
	
}
