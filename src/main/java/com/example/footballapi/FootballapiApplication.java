package com.example.footballapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FootballapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballapiApplication.class, args);
	}
}
