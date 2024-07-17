package com.swdgr6.bikeplatform;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication 
public class BikeplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeplatformApplication.class, args);
	}

}
