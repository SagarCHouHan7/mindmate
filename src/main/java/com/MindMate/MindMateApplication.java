package com.MindMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MindMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindMateApplication.class, args);
	}

}
