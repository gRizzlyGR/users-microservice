package com.grizzi.microservices.users.models.errors;

import io.swagger.annotations.ApiModelProperty;

/**
 * Error base model
 * @author giuseppe
 *
 */
public abstract class Error {
	protected String timestamp;
	protected Integer status;
	protected String error;
	protected String message;
	protected String path;

	protected Integer getStatus() {
		return status;
	}

	protected void setStatus(Integer status) {
		this.status = status;
	}

	protected String getError() {
		return error;
	}

	protected void setError(String error) {
		this.error = error;
	}

	protected String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	@ApiModelProperty(example = "1970-01-01T00:00:01", value = "Timestamp")
	public final String getTimestamp() {
		return timestamp;
	}

	public final void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@ApiModelProperty(example = "/users", value = "Path")
	public final String getPath() {
		return path;
	}

	public final void setPath(String path) {
		this.path = path;
	}

}
