package com.grizzi.microservices.users.models.errors.notfound;

import org.springframework.http.HttpStatus;

import com.grizzi.microservices.users.models.errors.Error;

import io.swagger.annotations.ApiModelProperty;

public abstract class NotFoundError extends Error {
	@Override
	protected void setStatus(Integer status) {
		super.setStatus(HttpStatus.NOT_FOUND.value());
	}

	@Override
	protected void setError(String error) {
		super.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
	}

	@Override
	@ApiModelProperty(example = "404", value = "Status")
	public Integer getStatus() {
		return super.getStatus();
	}

	@Override
	@ApiModelProperty(example = "Not Found", value = "Error")
	public String getError() {
		return super.getError();
	}
}
