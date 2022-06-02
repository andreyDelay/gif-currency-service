package com.andrey.gifcurrencyservice.config;

import com.andrey.gifcurrencyservice.exception.ApiBadRequestError;
import com.andrey.gifcurrencyservice.exception.CurrencyNotFoundException;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.BASIC;
	}

	@Bean
	ErrorDecoder errorDecoder() {
		return new FeignResponseStatusDecoder();
	}

	static class FeignResponseStatusDecoder implements ErrorDecoder {

		private ErrorDecoder errorDecoder = new Default();

		@Override
		public Exception decode(String s, Response response) {
			switch (response.status()) {
				case 400 -> throw new ApiBadRequestError("Bad request to API");
				case 404 -> throw new CurrencyNotFoundException("Currency code not found by API");
				default -> {
					return errorDecoder.decode(s, response);
				}
			}
		}
	}
}
