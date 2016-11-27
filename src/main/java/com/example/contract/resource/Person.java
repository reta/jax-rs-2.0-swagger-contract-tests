package com.example.contract.resource;

import java.util.UUID;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Person resource representation")
public class Person extends PersonUpdate {
	@ApiModelProperty(value = "Person's identifier", required = true) 
	private String id;

	public Person() {
	}
	
	public Person(PersonUpdate person) {
		this(person.getEmail(), person.getFirstName(), 
		    person.getLastName(), person.getAge());
	}

	public Person(String email, String firstName, String lastName, int age) {
		super(email, firstName, lastName, age);
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}