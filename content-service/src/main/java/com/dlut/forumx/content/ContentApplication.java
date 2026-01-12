package com.dlut.forumx.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
	}

}
