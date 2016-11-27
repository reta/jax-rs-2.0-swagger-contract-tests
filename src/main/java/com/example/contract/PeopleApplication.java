package com.example.contract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.contract.config.PeopleRestConfiguration;

@SpringBootApplication
public class PeopleApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeopleRestConfiguration.class, args);
	}
}