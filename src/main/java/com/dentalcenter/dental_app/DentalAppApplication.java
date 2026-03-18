package com.dentalcenter.dental_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DentalAppApplication {

	public static void main(String[] args) {
		System.out.println("--- L'APP STA PARTENDO ---");
		SpringApplication.run(DentalAppApplication.class, args);
		System.out.println("--- L'APP E' PARTITA ---");
	}

}
