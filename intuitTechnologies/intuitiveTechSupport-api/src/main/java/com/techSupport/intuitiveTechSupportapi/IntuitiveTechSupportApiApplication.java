package com.techSupport.intuitiveTechSupportapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class IntuitiveTechSupportApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntuitiveTechSupportApiApplication.class, args);
	}

}
