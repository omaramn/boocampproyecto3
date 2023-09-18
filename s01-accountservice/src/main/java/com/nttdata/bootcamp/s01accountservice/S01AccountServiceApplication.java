package com.nttdata.bootcamp.s01accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class S01AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(S01AccountServiceApplication.class, args);
	}

}
