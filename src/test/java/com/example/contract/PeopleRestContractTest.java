package com.example.contract;

import static io.restassured.RestAssured.given;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atlassian.oai.validator.pact.ValidatedPactProviderRule;
import com.example.contract.config.PeopleRestConfiguration;
import com.example.contract.resource.PersonUpdate;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import io.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = PeopleRestConfiguration.class)
public class PeopleRestContractTest {
	private static final String PROVIDER_ID = "People Rest Service";
	private static final String CONSUMER_ID = "People Rest Service Consumer";

	private ValidatedPactProviderRule provider;
	@Value("${local.server.port}")
	private int port;

	@Rule
	public ValidatedPactProviderRule getValidatedPactProviderRule() {
		if (provider == null) {
			provider = new ValidatedPactProviderRule("http://localhost:" + port + 
				"/services/swagger.json", null, PROVIDER_ID, this);
		}

		return provider;
	}

	@Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
	public PactFragment addPerson(PactDslWithProvider builder) {
		return builder
			.uponReceiving("POST new person")
			.method("POST")
			.path("/services/people/v1")
			.body(
				new PactDslJsonBody()
					.stringType("email")
					.stringType("firstName")
					.stringType("lastName")
					.numberType("age")
			)
			.willRespondWith()
			.status(201)
			.matchHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
			.body(
				new PactDslJsonBody()
					.uuid("id")
					.stringType("email")
					.stringType("firstName")
					.stringType("lastName")
					.numberType("age")
			)
			.toFragment();
	}
	
	@Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
	public PactFragment findPerson(PactDslWithProvider builder) {
		return builder
			.uponReceiving("GET find person")
			.method("GET")
			.path("/services/people/v1")
			.query("email=tom@smith.com")
			.willRespondWith()
			.status(200)
			.matchHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
			.body(
				new PactDslJsonBody()
					.uuid("id")
					.stringType("email")
					.stringType("firstName")
					.stringType("lastName")
					.numberType("age")
			)
			.toFragment();
	}
	
	@Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
	public PactFragment findNonExistingPerson(PactDslWithProvider builder) {
		return builder
			.uponReceiving("GET find non-existing person")
			.method("GET")
			.path("/services/people/v1")
			.query("email=tom@smith.com")
			.willRespondWith()
			.status(404)
			.matchHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
			.body(
				new PactDslJsonBody()
					.stringType("message")
			)
			.toFragment();
	}

	@Test
	@PactVerification(value = PROVIDER_ID, fragment = "addPerson")
	public void testAddPerson() {
		given()
			.contentType(ContentType.JSON)
			.body(new PersonUpdate("tom@smith.com", "Tom", "Smith", 60))
			.post(provider.getConfig().url() + "/services/people/v1")
			.then()
			.log()
			.all();
	}
	
	@Test
	@PactVerification(value = PROVIDER_ID, fragment = "findPerson")
	public void testFindPerson() {
		given()
			.contentType(ContentType.JSON)
			.queryParam("email", "tom@smith.com")
			.get(provider.getConfig().url() + "/services/people/v1")
			.then()
			.log()
			.all();
	}
	
	@Test
	@PactVerification(value = PROVIDER_ID, fragment = "findNonExistingPerson")
	public void testFindPersonWhichDoesNotExist() {
		given()
			.contentType(ContentType.JSON)
			.queryParam("email", "tom@smith.com")
			.get(provider.getConfig().url() + "/services/people/v1")
			.then()
			.log()
			.all();
	}
}
