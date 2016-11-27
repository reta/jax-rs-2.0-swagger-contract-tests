package com.example.contract.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Person resource representation")
public class PersonUpdate {
	@ApiModelProperty(value = "Person's first name", required = true) 
	private String email;
	@ApiModelProperty(value = "Person's e-mail address", required = true) 
	private String firstName;
	@ApiModelProperty(value = "Person's last name", required = true) 
	private String lastName;
	@ApiModelProperty(value = "Person's age", required = true) 
	private int age;

	public PersonUpdate() {
	}

	public PersonUpdate(String email, String firstName, String lastName, int age) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
}