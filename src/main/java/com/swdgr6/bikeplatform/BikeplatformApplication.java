package com.swdgr6.bikeplatform;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication 
public class BikeplatformApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("/Asia/Ho_Chi_Minh"));
		SpringApplication.run(BikeplatformApplication.class, args);
	}

}
