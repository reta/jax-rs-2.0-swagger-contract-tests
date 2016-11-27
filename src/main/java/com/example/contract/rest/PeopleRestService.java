package com.example.contract.rest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Component;

import com.example.contract.resource.Person;
import com.example.contract.resource.PersonUpdate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Component
@Api(value = "Manage people")
@Path("/people/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PeopleRestService {
	private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<String, Person>(); 
	
	@GET
	@ApiOperation(value = "Find person by e-mail", notes = "Find person by e-mail", response = Person.class)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Person with such e-mail doesn't exists", response = GenericError.class)
	})
	public Response findPerson(@ApiParam(value = "E-Mail address to lookup for", required = true) @QueryParam("email") final String email) {
		final Person person = persons.get(email);
		
		if (person == null) {
			return Response
				.status(Status.NOT_FOUND)
				.entity(new GenericError("User with such email does not exist: " + email))
				.build();
		}
		
		return Response.ok(person).build();
	}

	@POST
	@ApiOperation(value = "Create new person", notes = "Create new person", response = Person.class)
	@ApiResponses({
	    @ApiResponse(code = 201, message = "Person created successfully", response = Person.class),
	    @ApiResponse(code = 409, message = "Person with such e-mail already exists", response = GenericError.class)
	})
	public Response addPerson(@Context UriInfo uriInfo, @ApiParam(required = true) PersonUpdate person) {
		final Person newPerson = new Person(person);
		if (persons.putIfAbsent(person.getEmail(), newPerson) != null) {
			return Response
				.status(Status.CONFLICT)
				.entity(new GenericError("User with such email already exist: " + person.getEmail()))
				.build();
		} else {
			return Response
				.created(
					uriInfo
						.getRequestUriBuilder()
						.queryParam("email", person.getEmail())
						.build()
				)
				.entity(newPerson)
				.build();
		}
	}

}
