package com.andrey.gifcurrencyservice.exception;

import org.springframework.http.HttpStatus;

public class ApiBadRequestError extends ApiException {
	public ApiBadRequestError(String message) {
		super("API_NOT_SUPPORT_URL_ERROR", message, HttpStatus.BAD_REQUEST);
	}
}
