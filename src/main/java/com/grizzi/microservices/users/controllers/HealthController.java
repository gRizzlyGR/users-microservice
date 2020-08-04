package com.grizzi.microservices.users.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.grizzi.microservices.users.models.HealthReport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Health monitoring system", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthController {

	/**
	 * Redirect to Actuator health endpoint
	 * @param response
	 * @throws IOException
	 */
	@GetMapping(path = "/health")
	@ResponseStatus(code = HttpStatus.OK)
	@ApiOperation(value = "Health check")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Health check report", response = HealthReport.class),
	})
	public void getHealth(HttpServletResponse response) throws IOException {
		response.sendRedirect("/actuator/health");
	}
}
