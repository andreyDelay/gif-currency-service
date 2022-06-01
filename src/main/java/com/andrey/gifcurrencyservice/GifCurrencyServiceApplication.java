package com.andrey.gifcurrencyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GifCurrencyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GifCurrencyServiceApplication.class, args);
	}

}
