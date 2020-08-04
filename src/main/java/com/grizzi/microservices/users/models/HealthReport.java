package com.grizzi.microservices.users.models;

import io.swagger.annotations.ApiModelProperty;

public class HealthReport {

	private String status;
	private Object components;

	@ApiModelProperty(value = "Status", example = "UP")
	public String getStatus() {
		return status;
	}

	@ApiModelProperty(value = "Components")
	public Object getComponents() {
		return components;
	}
}
