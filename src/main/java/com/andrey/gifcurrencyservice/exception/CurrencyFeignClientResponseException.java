package com.andrey.gifcurrencyservice.exception;

import org.springframework.http.HttpStatus;

public class CurrencyFeignClientResponseException extends ApiException {

	public CurrencyFeignClientResponseException(String message) {
		super("CURRENCY_EXTERNAL_API_ERROR", message, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
