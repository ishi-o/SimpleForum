package com.dlut.simpleforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SimpleforumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleforumApplication.class, args);
	}

}
