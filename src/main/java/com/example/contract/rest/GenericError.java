package com.example.contract.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Generic error representation")
public class GenericError {
	@ApiModelProperty(value = "Error message", required = true)
	private String message;

	public GenericError() {
	}

	public GenericError(final String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
