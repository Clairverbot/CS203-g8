package com.G2T8.CS203WebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Cs203WebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cs203WebAppApplication.class, args);
	}

}
