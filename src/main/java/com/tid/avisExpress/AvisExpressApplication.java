package com.tid.avisExpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AvisExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvisExpressApplication.class, args);
	}

}
