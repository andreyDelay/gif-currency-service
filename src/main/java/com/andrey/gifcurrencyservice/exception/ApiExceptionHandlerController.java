package com.andrey.gifcurrencyservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler({})
	public ResponseEntity<ApiErrorResponse> handleGifApiException(ApiException apiException) {
		ApiErrorResponse apiErrorResponse =
				new ApiErrorResponse(apiException.getCode(), apiException.getMessage());
		return new ResponseEntity<>(apiErrorResponse, apiException.getHttpStatus());
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler({Exception.class})
	public ApiErrorResponse generalHandler(Exception e) {
		return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
}
