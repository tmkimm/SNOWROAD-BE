package com.snowroad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SnowroadStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnowroadStarterApplication.class, args);
	}

}
