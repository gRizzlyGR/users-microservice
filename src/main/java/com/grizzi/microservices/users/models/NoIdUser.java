package com.grizzi.microservices.users.models;

import io.swagger.annotations.ApiModelProperty;

/**
 * Used to hide the ID field in the POST request
 * @author giuseppe
 *
 */
public class NoIdUser extends User {

	@ApiModelProperty(hidden = true)
	@Override
	public Integer getId() {
		return super.getId();
	}
}
