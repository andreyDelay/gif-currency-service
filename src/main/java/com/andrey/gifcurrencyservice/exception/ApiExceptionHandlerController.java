package com.andrey.gifcurrencyservice.exception;

import com.andrey.gifcurrencyservice.dto.ApiErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<?> handleExceptions(ApiException apiException) {
		ApiErrorResponseDto apiErrorResponse =
				new ApiErrorResponseDto(apiException.getCode(), apiException.getMessage());
		return new ResponseEntity<>(apiErrorResponse, apiException.getHttpStatus());
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(Exception.class)
	public ApiErrorResponseDto generalHandler(Exception e) {
		return new ApiErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
}
