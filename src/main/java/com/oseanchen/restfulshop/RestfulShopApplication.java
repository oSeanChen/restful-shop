package com.oseanchen.restfulshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RestfulShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulShopApplication.class, args);
	}

}
