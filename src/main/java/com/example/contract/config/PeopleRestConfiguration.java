package com.example.contract.config;

import java.util.Arrays;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.example.contract.rest.PeopleRestService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = PeopleRestService.class)
public class PeopleRestConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	private PeopleRestService peopleRestService;

	@Bean @DependsOn("cxf")
	public Server jaxRsServer(SpringBus bus) {
		final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();

		factory.setServiceBean(peopleRestService);
		factory.setProvider(new JacksonJsonProvider());
		factory.setFeatures(Arrays.asList(swagger2Feature()));
		factory.setBus(bus);
		factory.setAddress("/");

		return factory.create();
	}
	
	@Bean
	public Swagger2Feature swagger2Feature() {
		final Swagger2Feature swagger2Feature = new Swagger2Feature();
		swagger2Feature.setBasePath("/services");
		swagger2Feature.setTitle("People Management");
		swagger2Feature.setDescription("People Management Microservice");
		swagger2Feature.setScan(true);
		swagger2Feature.setContact(null);
		swagger2Feature.setSupportSwaggerUi(false);
		return swagger2Feature;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/ui/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/2.2.6/");
	}
}
